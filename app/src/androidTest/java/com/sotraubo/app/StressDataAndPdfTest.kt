package com.sotraubo.app

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.common.format.ReportPeriodType
import com.sotraubo.app.core.data.repository.OperationOutcome
import com.sotraubo.app.core.database.entity.CurrencyExchangeEntity
import com.sotraubo.app.core.designsystem.component.TimeFilterMode
import com.sotraubo.app.core.model.CashFlowType
import com.sotraubo.app.core.model.DebtSortOption
import com.sotraubo.app.core.model.PaymentMethod
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.feature.backupreport.PdfReportCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

@RunWith(AndroidJUnit4::class)
class StressDataAndPdfTest {

    @Test
    fun seedHundredRecords_exportAndRenderMultiPagePdfs() = runBlocking {
        val app = ApplicationProvider.getApplicationContext<SoTrauBoApplication>()
        val container = app.container
        val dao = container.dao
        val ledgerDao = container.ledgerDao
        val repo = container.repository
        val pdfManager = container.backupAndPdfManager

        // Đặt PIN 111111 để dễ kiểm tra UI ngay sau test
        container.pinSecurityManager.setupNewPin("111111")

        val today = DateFormatter.todayNoonMillis()
        val now = System.currentTimeMillis()

        // 1. Nạp 25 đối tác qua Repository thật
        val existingContacts = dao.getAllContactsSnapshot()
        val contactIds = if (existingContacts.size >= 25) {
            existingContacts.map { it.id }
        } else {
            val names = listOf(
                "Anh Hùng", "Chị Lan", "Chú Bảy", "Cô Tư", "Anh Minh",
                "Anh Tuấn", "Chị Hoa", "Bác Sáu", "Anh Dũng", "Chị Mai",
                "Chú Năm", "Anh Khoa", "Chị Thảo", "Anh Phong", "Cô Nga",
                "Anh Bảo", "Chị Hương", "Chú Tám", "Anh Long", "Chị Ngọc",
                "Anh Sơn", "Chị Linh", "Bác Hai", "Anh Hải", "Chị Trang"
            )
            names.mapIndexed { idx, name ->
                val out = repo.saveContact(
                    fullName = name,
                    phone = "09${10000000 + idx * 123456}",
                    address = "Khu vực ${idx % 5 + 1}",
                    note = "Đối tác #${idx + 1}"
                )
                (out as OperationOutcome.Success).data.id
            }
        }

        // 2. Nạp 40 đơn Mua / Bán trâu bò qua Repository thật
        val existingOrders = dao.getAllTradeOrdersSnapshot()
        if (existingOrders.size < 40) {
            for (i in 0 until 40) {
                val isPurchase = i % 2 == 0
                val dayOffset = -(i % 12).toLong()
                val txDate = DateFormatter.addDays(today, dayOffset)
                val qty = (i % 4) + 1
                val total = qty * 16_500_000L + (i % 3) * 1_000_000L
                val paid = if (i % 3 == 0) total else total / 2L
                val contactId = contactIds[i % contactIds.size]
                repo.saveTradeOrder(
                    orderType = if (isPurchase) TradeOrderType.PURCHASE else TradeOrderType.SALE,
                    contactId = contactId,
                    transactionDate = txDate,
                    cattleQuantity = qty,
                    description = if (isPurchase) "Lô bò nhập #$i ($qty con)" else "Lô trâu xuất #$i ($qty con)",
                    totalAmount = total,
                    initialPaidAmount = paid,
                    initialPaymentMethod = if (i % 2 == 0) PaymentMethod.BANK_TRANSFER else PaymentMethod.CASH,
                    note = "Ghi chú đơn #$i"
                )
            }
        }

        // 3. Nạp 30 phiếu Thu / Chi tiền mặt & chuyển khoản
        val existingCash = dao.getAllCashTransactionsSnapshot()
        if (existingCash.size < 70) {
            for (i in 0 until 30) {
                val isCashIn = i % 2 == 0
                val dayOffset = -(i % 10).toLong()
                val txDate = DateFormatter.addDays(today, dayOffset)
                val contactId = contactIds[(i + 5) % contactIds.size]
                repo.saveCashTransaction(
                    flowType = if (isCashIn) CashFlowType.CASH_IN else CashFlowType.CASH_OUT,
                    contactId = contactId,
                    linkedTradeOrderId = null,
                    autoAllocateOldestDebt = false,
                    amount = 3_500_000L + (i % 5) * 1_200_000L,
                    transactionDate = txDate,
                    paymentMethod = if (i % 2 == 0) PaymentMethod.BANK_TRANSFER else PaymentMethod.CASH,
                    shortContent = if (isCashIn) "Thu tiền nợ #$i" else "Chuyển tiền cọc #$i",
                    note = "Phiếu #$i"
                )
            }
        }

        // 4. Nạp 30 giao dịch Bath Thái (có cả có tỷ giá và không tỷ giá)
        val existingExchanges = ledgerDao.getAllExchangesSnapshot()
        if (existingExchanges.size < 30) {
            for (i in 0 until 30) {
                val isMyTransfer = i % 2 == 0
                val dayOffset = -(i % 8).toLong()
                val exDate = DateFormatter.addDays(today, dayOffset)
                val baht = 15_000L + (i % 6) * 5_000L
                val rate = if (i % 4 == 0) 0L else 745L + (i % 15)
                val vnd = if (rate > 0L) baht * rate else 0L
                ledgerDao.upsertExchange(
                    CurrencyExchangeEntity(
                        exchangeDate = exDate,
                        direction = if (isMyTransfer) "VND_TO_THB" else "THB_TO_VND",
                        bahtAmount = baht,
                        rate = rate,
                        vndAmount = vnd,
                        note = "Đổi Bath #$i",
                        createdAt = now - i * 1500L
                    )
                )
            }
        }

        val summary = repo.observeDashboardSummary().first()
        val periodStats = repo.observePeriodBusinessStats(ReportPeriodType.THIS_MONTH).first()
        val receivables = repo.observeReceivableDebtors("", DebtSortOption.AMOUNT_DESC).first()
        val payables = repo.observePayableCreditors("", DebtSortOption.AMOUNT_DESC).first()

        // 5. Xuất 3 loại PDF (Buôn bán trâu bò, Đổi Bath Thái, Tất cả theo ngày)
        val outDir = app.getExternalFilesDir(null)!!
        outDir.mkdirs()

        val cattlePdf = File(outDir, "qc_pdf_cattle.pdf")
        val bathPdf = File(outDir, "qc_pdf_bath.pdf")
        val todayPdf = File(outDir, "qc_pdf_today.pdf")

        val res1 = pdfManager.exportOverviewReportPdf(
            context = app,
            destinationUri = Uri.fromFile(cattlePdf),
            summary = summary,
            periodStats = periodStats,
            receivableDebtors = receivables,
            payableCreditors = payables,
            category = PdfReportCategory.CATTLE_BUSINESS,
            timeFilterMode = TimeFilterMode.ALL,
            anchorMillis = today
        )
        assertTrue("Cattle PDF failed: $res1", res1 is OperationOutcome.Success)

        val res2 = pdfManager.exportOverviewReportPdf(
            context = app,
            destinationUri = Uri.fromFile(bathPdf),
            summary = summary,
            periodStats = periodStats,
            receivableDebtors = receivables,
            payableCreditors = payables,
            category = PdfReportCategory.BATH_EXCHANGE,
            timeFilterMode = TimeFilterMode.ALL,
            anchorMillis = today
        )
        assertTrue("Bath PDF failed: $res2", res2 is OperationOutcome.Success)

        val res3 = pdfManager.exportOverviewReportPdf(
            context = app,
            destinationUri = Uri.fromFile(todayPdf),
            summary = summary,
            periodStats = periodStats,
            receivableDebtors = receivables,
            payableCreditors = payables,
            category = PdfReportCategory.ALL,
            timeFilterMode = TimeFilterMode.BY_DAY,
            anchorMillis = today
        )
        assertTrue("Today PDF failed: $res3", res3 is OperationOutcome.Success)

        renderPdfPage(cattlePdf, 0, File(outDir, "qc_pdf_cattle_p1.png"))
        renderPdfPage(bathPdf, 0, File(outDir, "qc_pdf_bath_p1.png"))
        renderPdfPage(todayPdf, 0, File(outDir, "qc_pdf_today_p1.png"))
    }

    private fun renderPdfPage(pdfFile: File, pageIndex: Int, outPng: File) {
        assertTrue("PDF missing: ${pdfFile.absolutePath}", pdfFile.exists() && pdfFile.length() > 100)
        val pfd = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(pfd)
        assertTrue("PDF has no pages: ${pdfFile.name}", renderer.pageCount > pageIndex)
        val page = renderer.openPage(pageIndex)
        val bitmap = Bitmap.createBitmap(page.width * 2, page.height * 2, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.WHITE)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        FileOutputStream(outPng).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, fos)
        }
        page.close()
        renderer.close()
        pfd.close()
    }
}
