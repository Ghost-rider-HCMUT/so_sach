package com.sotraubo.app.feature.backupreport

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.sotraubo.app.core.common.dispatcher.AppDispatchers
import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.designsystem.component.TimeFilterMode
import com.sotraubo.app.core.data.repository.OperationOutcome
import com.sotraubo.app.core.database.dao.CattleBusinessDao
import com.sotraubo.app.core.database.dao.LedgerDao
import com.sotraubo.app.core.database.entity.AttachmentEntity
import com.sotraubo.app.core.database.entity.CashTransactionEntity
import com.sotraubo.app.core.database.entity.ContactEntity
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.database.entity.FamilyExpenseEntity
import com.sotraubo.app.core.database.entity.GoodsPurchaseEntity
import com.sotraubo.app.core.database.entity.QuotaFeeEntity
import com.sotraubo.app.core.database.entity.TradeOrderEntity
import com.sotraubo.app.core.model.DashboardFinancialSummary
import com.sotraubo.app.core.model.PeriodBusinessStats
import com.sotraubo.app.core.model.PersonBidirectionalDebtDetail
import com.sotraubo.app.core.model.PersonDebtSummaryItem
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

enum class PdfReportCategory(val label: String) {
    CATTLE_BUSINESS("Buôn bán hàng trâu bò"),
    BATH_EXCHANGE("Đổi Bath Thái"),
    ALL("Tất cả (Trâu bò & Đổi Bath)")
}

data class BackupPreviewInfo(
    val createdAtDisplay: String,
    val contactCount: Int,
    val tradeOrderCount: Int,
    val cashTransactionCount: Int,
    val attachmentCount: Int,
    val contacts: List<ContactEntity>,
    val tradeOrders: List<TradeOrderEntity>,
    val cashTransactions: List<CashTransactionEntity>,
    val attachments: List<AttachmentEntity>,
    val goodsPurchases: List<GoodsPurchaseEntity> = emptyList(),
    val currencyExchanges: List<CurrencyExchangeEntity> = emptyList(),
    val familyExpenses: List<FamilyExpenseEntity> = emptyList(),
    val quotaFees: List<QuotaFeeEntity> = emptyList()
)

/**
 * Bộ quản lý Sao lưu & Khôi phục dữ liệu (.zip chứa manifest.json + database_export.json + SHA-256)
 * và Xuất báo cáo PDF 100% Tiếng Việt.
 */
class BackupAndPdfManager(
    private val dao: CattleBusinessDao,
    private val ledgerDao: LedgerDao,
    private val dispatchers: AppDispatchers
) {

    companion object {
        private const val APP_IDENTIFIER = "com.sotraubo.app"
        private const val SCHEMA_VERSION = 2
        private const val MANIFEST_ENTRY = "manifest.json"
        private const val DATABASE_ENTRY = "database_export.json"
    }

    suspend fun clearAllDemoData(context: Context): OperationOutcome<String> =
        withContext(dispatchers.io) {
            try {
                dao.replaceAllDataInTransaction(emptyList(), emptyList(), emptyList(), emptyList())
                ledgerDao.replaceAllLedgerDataInTransaction(emptyList(), emptyList(), emptyList(), emptyList())
                context.getSharedPreferences("sotraubo_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putLong("initial_capital_vnd", 0L)
                    .apply()
                OperationOutcome.Success("Đã xóa toàn bộ dữ liệu. Bạn có thể bắt đầu lại từ đầu!")
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                OperationOutcome.Error("Không thể xóa dữ liệu: ${e.message ?: "Lỗi không xác định"}")
            }
        }

    suspend fun exportBackupToUri(context: Context, destinationUri: Uri): OperationOutcome<String> =
        withContext(dispatchers.io) {
            try {
                val contacts = dao.getAllContactsSnapshot()
                val orders = dao.getAllTradeOrdersSnapshot()
                val cashTransactions = dao.getAllCashTransactionsSnapshot()
                val attachments = dao.getAllAttachmentsSnapshot()
                val goods = ledgerDao.getAllGoodsSnapshot()
                val exchanges = ledgerDao.getAllExchangesSnapshot()
                val expenses = ledgerDao.getAllExpensesSnapshot()
                val quotaFees = ledgerDao.getAllQuotaFeesOnce()

                val dbJson = buildDatabaseJson(
                    contacts = contacts,
                    orders = orders,
                    cashTransactions = cashTransactions,
                    attachments = attachments,
                    goodsPurchases = goods,
                    currencyExchanges = exchanges,
                    familyExpenses = expenses,
                    quotaFees = quotaFees
                ).toString(2)
                val checksum = sha256Hex(dbJson.toByteArray(Charsets.UTF_8))
                val now = System.currentTimeMillis()
                val nowDisplay = DateFormatter.formatDateTime(now)

                val manifestJson = JSONObject().apply {
                    put("app_identifier", APP_IDENTIFIER)
                    put("schema_version", SCHEMA_VERSION)
                    put("created_at_epoch_ms", now)
                    put("created_at_display", nowDisplay)
                    put("contact_count", contacts.size)
                    put("trade_order_count", orders.size)
                    put("cash_transaction_count", cashTransactions.size)
                    put("attachment_count", attachments.size)
                    put("goods_purchase_count", goods.size)
                    put("currency_exchange_count", exchanges.size)
                    put("family_expense_count", expenses.size)
                    put("data_sha256_checksum", checksum)
                }.toString(2)

                context.contentResolver.openOutputStream(destinationUri)?.use { rawOut ->
                    ZipOutputStream(BufferedOutputStream(rawOut)).use { zipOut ->
                        zipOut.putNextEntry(ZipEntry(MANIFEST_ENTRY))
                        zipOut.write(manifestJson.toByteArray(Charsets.UTF_8))
                        zipOut.closeEntry()

                        zipOut.putNextEntry(ZipEntry(DATABASE_ENTRY))
                        zipOut.write(dbJson.toByteArray(Charsets.UTF_8))
                        zipOut.closeEntry()

                        attachments.forEach { att ->
                            val file = File(att.localFilePath)
                            if (file.exists() && file.isFile) {
                                zipOut.putNextEntry(ZipEntry("attachments/${file.name}"))
                                FileInputStream(file).use { fis -> fis.copyTo(zipOut) }
                                zipOut.closeEntry()
                            }
                        }
                    }
                } ?: return@withContext OperationOutcome.Error("Không thể mở tệp đích để lưu bản sao lưu.")

                OperationOutcome.Success(
                    "Đã sao lưu thành công ($nowDisplay): ${contacts.size} người liên quan, " +
                        "${orders.size} đơn mua/bán, ${cashTransactions.size} giao dịch tiền."
                )
            } catch (e: Exception) {
                OperationOutcome.Error("Lỗi khi xuất tệp sao lưu: ${e.localizedMessage ?: "Không xác định"}")
            }
        }

    /**
     * Kiểm tra tính hợp lệ của tệp sao lưu trước khi khôi phục (Mục 8 Master Prompt).
     * Trả về BackupPreviewInfo để hiển thị hộp thoại Cảnh báo ghi đè cho người dùng.
     */
    suspend fun validateBackupUri(context: Context, sourceUri: Uri): OperationOutcome<BackupPreviewInfo> =
        withContext(dispatchers.io) {
            try {
                var manifestText: String? = null
                var dbText: String? = null

                context.contentResolver.openInputStream(sourceUri)?.use { rawIn ->
                    ZipInputStream(BufferedInputStream(rawIn)).use { zipIn ->
                        var entry: ZipEntry? = zipIn.nextEntry
                        while (entry != null) {
                            when (entry.name) {
                                MANIFEST_ENTRY -> manifestText = zipIn.readBytes().toString(Charsets.UTF_8)
                                DATABASE_ENTRY -> dbText = zipIn.readBytes().toString(Charsets.UTF_8)
                            }
                            zipIn.closeEntry()
                            entry = zipIn.nextEntry
                        }
                    }
                } ?: return@withContext OperationOutcome.Error("Không thể đọc tệp đã chọn.")

                val safeManifestText = manifestText
                val safeDbText = dbText
                if (safeManifestText == null || safeDbText == null) {
                    return@withContext OperationOutcome.Error(
                        "Tệp đã chọn không phải là tệp sao lưu hợp lệ của ứng dụng Quản Lý."
                    )
                }

                val manifest = JSONObject(safeManifestText)
                val appId = manifest.optString("app_identifier", "")
                val schemaVersion = manifest.optInt("schema_version", -1)
                val expectedChecksum = manifest.optString("data_sha256_checksum", "")

                if (appId != APP_IDENTIFIER || schemaVersion <= 0 || schemaVersion > SCHEMA_VERSION) {
                    return@withContext OperationOutcome.Error(
                        "Phiên bản cấu trúc tệp sao lưu không tương thích với ứng dụng hiện tại."
                    )
                }

                val actualChecksum = sha256Hex(safeDbText.toByteArray(Charsets.UTF_8))
                if (expectedChecksum.isNotEmpty() && !expectedChecksum.equals(actualChecksum, ignoreCase = true)) {
                    return@withContext OperationOutcome.Error(
                        "Tệp sao lưu đã bị hỏng hoặc nội dung bị thay đổi. Hệ thống từ chối khôi phục để bảo vệ dữ liệu hiện tại."
                    )
                }

                val parsed = parseDatabaseJson(JSONObject(safeDbText), manifest.optString("created_at_display", "Không rõ"))
                OperationOutcome.Success(parsed)
            } catch (e: Exception) {
                OperationOutcome.Error("Tệp sao lưu không hợp lệ hoặc bị lỗi định dạng.")
            }
        }

    suspend fun confirmRestoreBackup(
        context: Context,
        sourceUri: Uri,
        preview: BackupPreviewInfo
    ): OperationOutcome<String> = withContext(dispatchers.io) {
        try {
            val attachmentDir = File(context.filesDir, "attachments").apply { if (!exists()) mkdirs() }

            context.contentResolver.openInputStream(sourceUri)?.use { rawIn ->
                ZipInputStream(BufferedInputStream(rawIn)).use { zipIn ->
                    var entry: ZipEntry? = zipIn.nextEntry
                    while (entry != null) {
                        if (entry.name.startsWith("attachments/") && !entry.isDirectory) {
                            val fileName = File(entry.name).name
                            val destFile = File(attachmentDir, fileName)
                            FileOutputStream(destFile).use { fos ->
                                zipIn.copyTo(fos)
                            }
                        }
                        zipIn.closeEntry()
                        entry = zipIn.nextEntry
                    }
                }
            }

            val remappedAttachments = preview.attachments.map { att ->
                val fileName = File(att.localFilePath).name
                val localPath = File(attachmentDir, fileName).absolutePath
                att.copy(localFilePath = localPath)
            }

            dao.replaceAllDataInTransaction(
                contacts = preview.contacts,
                orders = preview.tradeOrders,
                cashTransactions = preview.cashTransactions,
                attachments = remappedAttachments
            )
            ledgerDao.replaceAllLedgerDataInTransaction(
                goods = preview.goodsPurchases,
                exchanges = preview.currencyExchanges,
                expenses = preview.familyExpenses,
                quotaFees = preview.quotaFees
            )

            OperationOutcome.Success(
                "Đã khôi phục dữ liệu thành công từ bản sao lưu ngày ${preview.createdAtDisplay}!"
            )
        } catch (e: Exception) {
            OperationOutcome.Error("Khôi phục thất bại. Dữ liệu hiện tại được giữ nguyên an toàn.")
        }
    }

    /**
     * Xuất báo cáo PDF theo loại (Buôn bán hàng trâu bò / Đổi Bath Thái / Tất cả) và theo ngày/tháng.
     */
    suspend fun exportOverviewReportPdf(
        context: Context,
        destinationUri: Uri,
        summary: DashboardFinancialSummary,
        periodStats: PeriodBusinessStats,
        receivableDebtors: List<PersonDebtSummaryItem>,
        payableCreditors: List<PersonDebtSummaryItem>,
        category: PdfReportCategory = PdfReportCategory.ALL,
        timeFilterMode: TimeFilterMode = TimeFilterMode.ALL,
        anchorMillis: Long = DateFormatter.todayNoonMillis()
    ): OperationOutcome<String> = withContext(dispatchers.io) {
        try {
            val pdfDocument = PdfDocument()
            var pageNum = 1
            var page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNum).create())
            var canvas: Canvas = page.canvas

            val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(21, 101, 192)
                textSize = 17f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val sectionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(17, 24, 39)
                textSize = 13f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(31, 41, 55)
                textSize = 11f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            }
            val linePaint = Paint().apply {
                color = Color.LTGRAY
                strokeWidth = 1f
            }

            var y = 45f
            fun ensureSpace(needed: Float = 20f) {
                if (y + needed > 800f) {
                    pdfDocument.finishPage(page)
                    pageNum += 1
                    page = pdfDocument.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNum).create())
                    canvas = page.canvas
                    y = 45f
                }
            }

            fun matchesFilter(millis: Long): Boolean = when (timeFilterMode) {
                TimeFilterMode.ALL -> true
                TimeFilterMode.BY_MONTH -> DateFormatter.isSameMonth(millis, anchorMillis)
                TimeFilterMode.BY_DAY -> DateFormatter.isSameDay(millis, anchorMillis)
                TimeFilterMode.TODAY -> DateFormatter.isSameDay(millis, DateFormatter.todayNoonMillis())
            }

            val periodText = when (timeFilterMode) {
                TimeFilterMode.ALL -> "Tất cả thời gian"
                TimeFilterMode.BY_MONTH -> DateFormatter.formatMonthYear(anchorMillis)
                TimeFilterMode.BY_DAY -> DateFormatter.formatDate(anchorMillis)
                TimeFilterMode.TODAY -> "Hôm nay (${DateFormatter.formatDate(DateFormatter.todayNoonMillis())})"
            }

            canvas.drawText("BÁO CÁO QUẢN LÝ: ${category.label.uppercase()}", 40f, y, titlePaint)
            y += 18f
            canvas.drawText("Kỳ báo cáo: $periodText  |  Xuất lúc: ${DateFormatter.formatDateTime(System.currentTimeMillis())}", 40f, y, bodyPaint)
            y += 14f
            canvas.drawLine(40f, y, 555f, y, linePaint)

            if (category == PdfReportCategory.CATTLE_BUSINESS || category == PdfReportCategory.ALL) {
                val filteredOrders = dao.getAllTradeOrdersSnapshot().filter { matchesFilter(it.transactionDate) }
                val filteredCash = dao.getAllCashTransactionsSnapshot().filter { matchesFilter(it.transactionDate) }
                val purchaseSum = filteredOrders.filter { it.orderType == "PURCHASE" }.sumOf { it.totalAmount }
                val purchaseCount = filteredOrders.filter { it.orderType == "PURCHASE" }.sumOf { it.cattleQuantity }
                val saleSum = filteredOrders.filter { it.orderType == "SALE" }.sumOf { it.totalAmount }
                val saleCount = filteredOrders.filter { it.orderType == "SALE" }.sumOf { it.cattleQuantity }
                val cashInSum = filteredCash.filter { it.flowType == "CASH_IN" }.sumOf { it.amount }
                val cashOutSum = filteredCash.filter { it.flowType == "CASH_OUT" }.sumOf { it.amount }

                y += 22f
                canvas.drawText("I. BUÔN BÁN HÀNG TRÂU BÒ ($periodText)", 40f, y, sectionPaint)
                y += 18f
                canvas.drawText("• Mua hàng ($purchaseCount con): ${CurrencyFormatter.formatVnd(purchaseSum)}", 50f, y, bodyPaint)
                y += 16f
                canvas.drawText("• Bán hàng ($saleCount con): ${CurrencyFormatter.formatVnd(saleSum)}", 50f, y, bodyPaint)
                y += 16f
                canvas.drawText("• Tiền đã nhận: ${CurrencyFormatter.formatVnd(cashInSum)}  |  Tiền đã chuyển: ${CurrencyFormatter.formatVnd(cashOutSum)}", 50f, y, bodyPaint)
                y += 16f
                canvas.drawText("• Tổng công nợ phải thu (${summary.receivableDebtorCount} người): ${CurrencyFormatter.formatVnd(summary.totalReceivableDebt)}", 50f, y, bodyPaint)
                y += 16f
                canvas.drawText("• Tổng công nợ phải trả (${summary.payableCreditorCount} người): ${CurrencyFormatter.formatVnd(summary.totalPayableDebt)}", 50f, y, bodyPaint)

                y += 22f
                ensureSpace(30f)
                canvas.drawText("• Công nợ phải thu (${receivableDebtors.size} người)", 40f, y, sectionPaint)
                y += 16f
                if (receivableDebtors.isEmpty()) {
                    canvas.drawText("  Không có công nợ phải thu.", 50f, y, bodyPaint)
                    y += 16f
                } else {
                    receivableDebtors.forEachIndexed { idx, p ->
                        ensureSpace()
                        val phonePart = if (p.phone.isNotBlank()) " (${p.phone})" else ""
                        canvas.drawText("  ${idx + 1}. ${p.fullName}$phonePart", 50f, y, bodyPaint)
                        canvas.drawText(CurrencyFormatter.formatVnd(p.remainingDebt), 430f, y, sectionPaint)
                        y += 16f
                    }
                }

                y += 12f
                ensureSpace(30f)
                canvas.drawText("• Công nợ phải trả (${payableCreditors.size} người)", 40f, y, sectionPaint)
                y += 16f
                if (payableCreditors.isEmpty()) {
                    canvas.drawText("  Không có công nợ phải trả.", 50f, y, bodyPaint)
                    y += 16f
                } else {
                    payableCreditors.forEachIndexed { idx, p ->
                        ensureSpace()
                        val phonePart = if (p.phone.isNotBlank()) " (${p.phone})" else ""
                        canvas.drawText("  ${idx + 1}. ${p.fullName}$phonePart", 50f, y, bodyPaint)
                        canvas.drawText(CurrencyFormatter.formatVnd(p.remainingDebt), 430f, y, sectionPaint)
                        y += 16f
                    }
                }
            }

            if (category == PdfReportCategory.BATH_EXCHANGE || category == PdfReportCategory.ALL) {
                val exchangesList = ledgerDao.getAllExchangesSnapshot().filter { matchesFilter(it.exchangeDate) }
                val myBath = exchangesList.sumOf { it.bahtAmount }
                val myVnd = exchangesList.sumOf { it.resolvedCostVnd() }
                val theirTheoretical = exchangesList
                    .filter { it.direction == "THB_TO_VND" }
                    .sumOf { if (it.theoreticalVndAmount > 0L) it.theoreticalVndAmount else it.vndAmount }
                val theirVnd = exchangesList.filter { it.direction == "THB_TO_VND" }.sumOf { it.vndAmount }
                val bathDebt = (theirTheoretical - theirVnd).coerceAtLeast(0L)
                val actualProfit = theirVnd - myVnd
                val expectedProfit = theirTheoretical - myVnd

                y += 22f
                ensureSpace(110f)
                val sectionTitle = if (category == PdfReportCategory.ALL) "II. SỔ ĐỔI BATH THÁI ($periodText)" else "I. SỔ ĐỔI BATH THÁI ($periodText)"
                canvas.drawText(sectionTitle, 40f, y, sectionPaint)
                y += 18f
                canvas.drawText("• Tiền đã chuyển đi: ${CurrencyFormatter.formatVnd(myVnd)} (${CurrencyFormatter.formatNumberOnly(myBath)} ฿)", 50f, y, bodyPaint)
                y += 16f
                canvas.drawText("• Tiền cần thu về: ${CurrencyFormatter.formatVnd(theirTheoretical)}", 50f, y, bodyPaint)
                y += 16f
                canvas.drawText("• Tiền đã nhận về: ${CurrencyFormatter.formatVnd(theirVnd)}", 50f, y, bodyPaint)
                y += 16f
                canvas.drawText("• Khách còn thiếu (Nợ Bath): ${CurrencyFormatter.formatVnd(bathDebt)}", 50f, y, bodyPaint)
                y += 16f
                canvas.drawText("• Lời/Lỗ thực tế: ${CurrencyFormatter.formatVnd(actualProfit)}  |  Dự kiến: ${CurrencyFormatter.formatVnd(expectedProfit)}", 50f, y, bodyPaint)
                y += 18f

                if (exchangesList.isEmpty()) {
                    canvas.drawText("  Không có giao dịch Bath trong kỳ này.", 50f, y, bodyPaint)
                    y += 16f
                } else {
                    exchangesList.forEachIndexed { idx, item ->
                        ensureSpace()
                        val sentVnd = item.resolvedCostVnd()
                        val receivedVnd = if (item.direction == "THB_TO_VND") item.vndAmount else 0L
                        val targetVnd = if (item.direction == "THB_TO_VND") {
                            if (item.theoreticalVndAmount > 0L) item.theoreticalVndAmount else item.vndAmount
                        } else {
                            0L
                        }
                        val bathStr = if (item.bahtAmount > 0L) "${CurrencyFormatter.formatNumberOnly(item.bahtAmount)} ฿ | " else ""
                        val moneyDetail = buildString {
                            append("Chuyển: ${CurrencyFormatter.formatVnd(sentVnd)}")
                            if (item.direction == "THB_TO_VND") {
                                append(" | Nhận: ${CurrencyFormatter.formatVnd(receivedVnd)}")
                                if (targetVnd > receivedVnd) {
                                    append(" / Cần thu: ${CurrencyFormatter.formatVnd(targetVnd)}")
                                }
                            }
                        }
                        val noteText = if (item.note.isNotBlank()) " (${item.note})" else ""
                        canvas.drawText(
                            "${idx + 1}. ${DateFormatter.formatDate(item.exchangeDate)} - $bathStr$moneyDetail$noteText",
                            50f, y, bodyPaint
                        )
                        y += 16f
                    }
                }
            }

            pdfDocument.finishPage(page)
            context.contentResolver.openOutputStream(destinationUri)?.use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()

            OperationOutcome.Success("Đã xuất báo cáo tổng hợp ra tệp PDF thành công!")
        } catch (e: Exception) {
            OperationOutcome.Error("Không thể xuất tệp PDF: ${e.localizedMessage ?: "Lỗi ghi tệp"}")
        }
    }

    /**
     * Xuất Phiếu Đối Chiếu Công Nợ 2 Chiều của 1 người ra tệp PDF trong bộ nhớ cache và mở hộp thoại Chia sẻ (Zalo / In).
     */
    suspend fun exportAndSharePersonDebtPdf(
        context: Context,
        detail: PersonBidirectionalDebtDetail
    ): OperationOutcome<String> = withContext(dispatchers.io) {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(21, 101, 192)
                textSize = 17f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val sectionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(17, 24, 39)
                textSize = 12.5f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.rgb(31, 41, 55)
                textSize = 11f
            }

            var y = 45f
            canvas.drawText("PHIẾU ĐỐI CHIẾU CÔNG NỢ & LỊCH SỬ GIAO DỊCH", 40f, y, titlePaint)
            y += 20f
            canvas.drawText("Họ và tên đối tác: ${detail.contact.fullName}   |   SĐT: ${detail.contact.phone.ifBlank { "Không có" }}", 40f, y, sectionPaint)
            y += 16f
            canvas.drawText("Ngày đối chiếu: ${DateFormatter.formatDateTime(System.currentTimeMillis())}", 40f, y, bodyPaint)

            y += 24f
            canvas.drawText("1. PHẦN ${detail.contact.fullName.uppercase()} CÒN NỢ MÌNH (PHẢI THU):", 40f, y, sectionPaint)
            y += 16f
            canvas.drawText("• Tổng tiền bán hàng: ${CurrencyFormatter.formatVnd(detail.initialReceivableTotal)}", 50f, y, bodyPaint)
            y += 15f
            canvas.drawText("• Đã thanh toán (Đã nhận): ${CurrencyFormatter.formatVnd(detail.paidReceivableTotal)}", 50f, y, bodyPaint)
            y += 16f
            canvas.drawText("=> SỐ TIỀN CÒN NỢ HIỆN TẠI: ${CurrencyFormatter.formatVnd(detail.currentReceivableDebt)}", 50f, y, sectionPaint)

            y += 24f
            canvas.drawText("2. PHẦN MÌNH CÒN NỢ ${detail.contact.fullName.uppercase()} (PHẢI TRẢ):", 40f, y, sectionPaint)
            y += 16f
            canvas.drawText("• Tổng tiền mua hàng: ${CurrencyFormatter.formatVnd(detail.initialPayableTotal)}", 50f, y, bodyPaint)
            y += 15f
            canvas.drawText("• Đã thanh toán (Đã trả): ${CurrencyFormatter.formatVnd(detail.paidPayableTotal)}", 50f, y, bodyPaint)
            y += 16f
            canvas.drawText("=> SỐ TIỀN MÌNH CÒN NỢ HIỆN TẠI: ${CurrencyFormatter.formatVnd(detail.currentPayableDebt)}", 50f, y, sectionPaint)

            y += 24f
            canvas.drawText("3. LỊCH SỬ CÁC ĐƠN MUA / BÁN GẦN ĐÂY:", 40f, y, sectionPaint)
            y += 18f
            (detail.saleOrders + detail.purchaseOrders)
                .sortedByDescending { it.transactionDate }
                .take(15)
                .forEach { order ->
                    val line = "${DateFormatter.formatDate(order.transactionDate)} | ${order.orderType.vietnameseLabel} (${order.cattleQuantity} con): ${order.description} | Tổng: ${CurrencyFormatter.formatVnd(order.totalAmount)} | Nợ: ${CurrencyFormatter.formatVnd(order.remainingDebt)}"
                    canvas.drawText(line.take(92), 45f, y, bodyPaint)
                    y += 15f
                }

            pdfDocument.finishPage(page)

            val reportsDir = File(context.filesDir, "reports").apply { if (!exists()) mkdirs() }
            val outFile = File(
                reportsDir,
                "PhieuDoiChieu_${detail.contact.nameNormalized.replace(" ", "_")}.pdf"
            )
            FileOutputStream(outFile).use { out ->
                pdfDocument.writeTo(out)
            }
            pdfDocument.close()

            OperationOutcome.Success("Đã tạo phiếu PDF tại: ${outFile.name}")
        } catch (e: Exception) {
            OperationOutcome.Error("Không thể tạo phiếu PDF: ${e.localizedMessage ?: "Lỗi"}")
        }
    }

    internal fun buildDatabaseJson(
        contacts: List<ContactEntity>,
        orders: List<TradeOrderEntity>,
        cashTransactions: List<CashTransactionEntity>,
        attachments: List<AttachmentEntity>,
        goodsPurchases: List<GoodsPurchaseEntity> = emptyList(),
        currencyExchanges: List<CurrencyExchangeEntity> = emptyList(),
        familyExpenses: List<FamilyExpenseEntity> = emptyList(),
        quotaFees: List<QuotaFeeEntity> = emptyList()
    ): JSONObject {
        val root = JSONObject()

        val contactsArr = JSONArray()
        contacts.forEach { c ->
            contactsArr.put(
                JSONObject().apply {
                    put("id", c.id)
                    put("full_name", c.fullName)
                    put("name_normalized", c.nameNormalized)
                    put("phone", c.phone)
                    put("phone_normalized", c.phoneNormalized)
                    put("address", c.address)
                    put("note", c.note)
                    put("created_at", c.createdAt)
                    put("updated_at", c.updatedAt)
                }
            )
        }
        root.put("contacts", contactsArr)

        val ordersArr = JSONArray()
        orders.forEach { o ->
            ordersArr.put(
                JSONObject().apply {
                    put("id", o.id)
                    put("order_type", o.orderType)
                    put("contact_id", o.contactId)
                    put("transaction_date", o.transactionDate)
                    put("cattle_quantity", o.cattleQuantity)
                    put("description", o.description)
                    put("total_amount", o.totalAmount)
                    put("note", o.note)
                    put("created_at", o.createdAt)
                    put("updated_at", o.updatedAt)
                }
            )
        }
        root.put("trade_orders", ordersArr)

        val cashArr = JSONArray()
        cashTransactions.forEach { ct ->
            cashArr.put(
                JSONObject().apply {
                    put("id", ct.id)
                    put("flow_type", ct.flowType)
                    put("contact_id", ct.contactId)
                    if (ct.tradeOrderId != null) put("trade_order_id", ct.tradeOrderId)
                    put("is_initial_payment", ct.isInitialPayment)
                    put("amount", ct.amount)
                    put("transaction_date", ct.transactionDate)
                    put("payment_method", ct.paymentMethod)
                    put("short_content", ct.shortContent)
                    put("note", ct.note)
                    put("created_at", ct.createdAt)
                    put("updated_at", ct.updatedAt)
                }
            )
        }
        root.put("cash_transactions", cashArr)

        val attArr = JSONArray()
        attachments.forEach { a ->
            attArr.put(
                JSONObject().apply {
                    put("id", a.id)
                    if (a.tradeOrderId != null) put("trade_order_id", a.tradeOrderId)
                    if (a.cashTransactionId != null) put("cash_transaction_id", a.cashTransactionId)
                    put("local_file_path", a.localFilePath)
                    put("created_at", a.createdAt)
                }
            )
        }
        root.put("attachments", attArr)

        val goodsArr = JSONArray()
        goodsPurchases.forEach { g ->
            goodsArr.put(
                JSONObject().apply {
                    put("id", g.id)
                    put("purchase_date", g.purchaseDate)
                    put("item_name", g.itemName)
                    put("quantity_x100", g.quantityX100)
                    put("unit", g.unit)
                    put("total_amount", g.totalAmount)
                    put("supplier", g.supplier)
                    if (g.contactId != null) put("contact_id", g.contactId) else put("contact_id", JSONObject.NULL)
                    put("note", g.note)
                    put("created_at", g.createdAt)
                }
            )
        }
        root.put("goods_purchases", goodsArr)

        val exArr = JSONArray()
        currencyExchanges.forEach { x ->
            exArr.put(
                JSONObject().apply {
                    put("id", x.id)
                    put("exchange_date", x.exchangeDate)
                    put("direction", x.direction)
                    put("baht_amount", x.bahtAmount)
                    put("rate", x.rate)
                    put("vnd_amount", x.vndAmount)
                    put("theoretical_vnd_amount", x.theoreticalVndAmount)
                    put("cost_rate", x.costRate)
                    put("cost_vnd_amount", x.costVndAmount)
                    put("partner_name", x.partnerName)
                    if (x.contactId != null) put("contact_id", x.contactId) else put("contact_id", JSONObject.NULL)
                    put("note", x.note)
                    put("created_at", x.createdAt)
                }
            )
        }
        root.put("currency_exchanges", exArr)

        val expArr = JSONArray()
        familyExpenses.forEach { e ->
            expArr.put(
                JSONObject().apply {
                    put("id", e.id)
                    put("expense_date", e.expenseDate)
                    put("category", e.category)
                    put("amount", e.amount)
                    put("partner_name", e.partnerName)
                    if (e.contactId != null) put("contact_id", e.contactId) else put("contact_id", JSONObject.NULL)
                    put("note", e.note)
                    put("created_at", e.createdAt)
                }
            )
        }
        root.put("family_expenses", expArr)

        val quotaArr = JSONArray()
        quotaFees.forEach { q ->
            quotaArr.put(
                JSONObject().apply {
                    put("id", q.id)
                    put("fee_date", q.feeDate)
                    put("payer_name", q.payerName)
                    if (q.contactId != null) put("contact_id", q.contactId) else put("contact_id", JSONObject.NULL)
                    put("cattle_quantity", q.cattleQuantity)
                    put("amount", q.amount)
                    put("note", q.note)
                    put("created_at", q.createdAt)
                }
            )
        }
        root.put("quota_fees", quotaArr)

        return root
    }

    internal fun parseDatabaseJson(root: JSONObject, createdAtDisplay: String): BackupPreviewInfo {
        val contactsArr = root.getJSONArray("contacts")
        val contacts = mutableListOf<ContactEntity>()
        for (i in 0 until contactsArr.length()) {
            val obj = contactsArr.getJSONObject(i)
            val fullName = obj.getString("full_name")
            require(fullName.isNotBlank()) { "Tên người liên quan trống" }
            contacts.add(
                ContactEntity(
                    id = obj.getLong("id"),
                    fullName = fullName,
                    nameNormalized = obj.optString("name_normalized", ""),
                    phone = obj.optString("phone", ""),
                    phoneNormalized = obj.optString("phone_normalized", ""),
                    address = obj.optString("address", ""),
                    note = obj.optString("note", ""),
                    createdAt = obj.optLong("created_at", System.currentTimeMillis()),
                    updatedAt = obj.optLong("updated_at", System.currentTimeMillis())
                )
            )
        }

        val ordersArr = root.getJSONArray("trade_orders")
        val orders = mutableListOf<TradeOrderEntity>()
        for (i in 0 until ordersArr.length()) {
            val obj = ordersArr.getJSONObject(i)
            val totalAmount = obj.getLong("total_amount")
            require(totalAmount > 0L) { "Tổng tiền đơn mua/bán không hợp lệ" }
            orders.add(
                TradeOrderEntity(
                    id = obj.getLong("id"),
                    orderType = obj.getString("order_type"),
                    contactId = obj.getLong("contact_id"),
                    transactionDate = obj.getLong("transaction_date"),
                    cattleQuantity = obj.getInt("cattle_quantity"),
                    description = obj.getString("description"),
                    totalAmount = totalAmount,
                    note = obj.optString("note", ""),
                    createdAt = obj.optLong("created_at", System.currentTimeMillis()),
                    updatedAt = obj.optLong("updated_at", System.currentTimeMillis())
                )
            )
        }

        val cashArr = root.getJSONArray("cash_transactions")
        val cashTransactions = mutableListOf<CashTransactionEntity>()
        for (i in 0 until cashArr.length()) {
            val obj = cashArr.getJSONObject(i)
            val amount = obj.getLong("amount")
            require(amount > 0L) { "Số tiền giao dịch không hợp lệ" }
            cashTransactions.add(
                CashTransactionEntity(
                    id = obj.getLong("id"),
                    flowType = obj.getString("flow_type"),
                    contactId = obj.getLong("contact_id"),
                    tradeOrderId = if (obj.has("trade_order_id") && !obj.isNull("trade_order_id")) {
                        obj.getLong("trade_order_id")
                    } else null,
                    isInitialPayment = obj.optBoolean("is_initial_payment", false),
                    amount = amount,
                    transactionDate = obj.getLong("transaction_date"),
                    paymentMethod = obj.getString("payment_method"),
                    shortContent = obj.getString("short_content"),
                    note = obj.optString("note", ""),
                    createdAt = obj.optLong("created_at", System.currentTimeMillis()),
                    updatedAt = obj.optLong("updated_at", System.currentTimeMillis())
                )
            )
        }

        val attArr = root.optJSONArray("attachments") ?: JSONArray()
        val attachments = mutableListOf<AttachmentEntity>()
        for (i in 0 until attArr.length()) {
            val obj = attArr.getJSONObject(i)
            attachments.add(
                AttachmentEntity(
                    id = obj.getLong("id"),
                    tradeOrderId = if (obj.has("trade_order_id") && !obj.isNull("trade_order_id")) {
                        obj.getLong("trade_order_id")
                    } else null,
                    cashTransactionId = if (obj.has("cash_transaction_id") && !obj.isNull("cash_transaction_id")) {
                        obj.getLong("cash_transaction_id")
                    } else null,
                    localFilePath = obj.getString("local_file_path"),
                    createdAt = obj.optLong("created_at", System.currentTimeMillis())
                )
            )
        }

        val goodsArr = root.optJSONArray("goods_purchases") ?: JSONArray()
        val goodsPurchases = mutableListOf<GoodsPurchaseEntity>()
        for (i in 0 until goodsArr.length()) {
            val obj = goodsArr.getJSONObject(i)
            goodsPurchases.add(
                GoodsPurchaseEntity(
                    id = obj.getLong("id"),
                    purchaseDate = obj.getLong("purchase_date"),
                    itemName = obj.getString("item_name"),
                    quantityX100 = obj.getLong("quantity_x100"),
                    unit = obj.getString("unit"),
                    totalAmount = obj.getLong("total_amount"),
                    supplier = obj.optString("supplier", ""),
                    contactId = if (obj.isNull("contact_id")) null else obj.optLong("contact_id").takeIf { it > 0L },
                    note = obj.optString("note", ""),
                    createdAt = obj.optLong("created_at", System.currentTimeMillis())
                )
            )
        }

        val exArr = root.optJSONArray("currency_exchanges") ?: JSONArray()
        val currencyExchanges = mutableListOf<CurrencyExchangeEntity>()
        for (i in 0 until exArr.length()) {
            val obj = exArr.getJSONObject(i)
            currencyExchanges.add(
                CurrencyExchangeEntity(
                    id = obj.getLong("id"),
                    exchangeDate = obj.getLong("exchange_date"),
                    direction = obj.getString("direction"),
                    bahtAmount = obj.getLong("baht_amount"),
                    rate = obj.getLong("rate"),
                    vndAmount = obj.getLong("vnd_amount"),
                    theoreticalVndAmount = obj.optLong("theoretical_vnd_amount", 0L),
                    costRate = obj.optLong("cost_rate", 0L),
                    costVndAmount = obj.optLong("cost_vnd_amount", 0L),
                    partnerName = obj.optString("partner_name", ""),
                    contactId = if (obj.isNull("contact_id")) null else obj.optLong("contact_id").takeIf { it > 0L },
                    note = obj.optString("note", ""),
                    createdAt = obj.optLong("created_at", System.currentTimeMillis())
                )
            )
        }

        val expArr = root.optJSONArray("family_expenses") ?: JSONArray()
        val familyExpenses = mutableListOf<FamilyExpenseEntity>()
        for (i in 0 until expArr.length()) {
            val obj = expArr.getJSONObject(i)
            familyExpenses.add(
                FamilyExpenseEntity(
                    id = obj.getLong("id"),
                    expenseDate = obj.getLong("expense_date"),
                    category = obj.optString("category", ""),
                    amount = obj.getLong("amount"),
                    partnerName = obj.optString("partner_name", ""),
                    contactId = if (obj.isNull("contact_id")) null else obj.optLong("contact_id").takeIf { it > 0L },
                    note = obj.optString("note", ""),
                    createdAt = obj.optLong("created_at", System.currentTimeMillis())
                )
            )
        }

        val quotaArr = root.optJSONArray("quota_fees") ?: JSONArray()
        val quotaFees = mutableListOf<QuotaFeeEntity>()
        for (i in 0 until quotaArr.length()) {
            val obj = quotaArr.getJSONObject(i)
            quotaFees.add(
                QuotaFeeEntity(
                    id = obj.getLong("id"),
                    feeDate = obj.getLong("fee_date"),
                    payerName = obj.optString("payer_name", ""),
                    contactId = if (obj.isNull("contact_id")) null else obj.optLong("contact_id").takeIf { it > 0L },
                    cattleQuantity = obj.optInt("cattle_quantity", 0),
                    amount = obj.getLong("amount"),
                    note = obj.optString("note", ""),
                    createdAt = obj.optLong("created_at", System.currentTimeMillis())
                )
            )
        }

        return BackupPreviewInfo(
            createdAtDisplay = createdAtDisplay,
            contactCount = contacts.size,
            tradeOrderCount = orders.size,
            cashTransactionCount = cashTransactions.size,
            attachmentCount = attachments.size,
            contacts = contacts,
            tradeOrders = orders,
            cashTransactions = cashTransactions,
            attachments = attachments,
            goodsPurchases = goodsPurchases,
            currencyExchanges = currencyExchanges,
            familyExpenses = familyExpenses,
            quotaFees = quotaFees
        )
    }

    private fun CurrencyExchangeEntity.resolvedCostVnd(): Long = when (direction) {
        "VND_TO_THB" -> vndAmount
        "THB_TO_VND" -> when {
            costVndAmount > 0L -> costVndAmount
            bahtAmount > 0L && costRate > 0L -> bahtAmount * costRate
            else -> 0L
        }
        else -> 0L
    }

    private fun sha256Hex(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
}
