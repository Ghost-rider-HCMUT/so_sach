package com.sotraubo.app.core.data.repository

import androidx.room.withTransaction
import com.sotraubo.app.core.common.dispatcher.AppDispatchers
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.common.format.ReportPeriodType
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer
import com.sotraubo.app.core.data.FinancialRuleValidator
import com.sotraubo.app.core.data.ValidationResult
import com.sotraubo.app.core.database.CattleBusinessDatabase
import com.sotraubo.app.core.database.dao.CashTransactionQueryProjection
import com.sotraubo.app.core.database.dao.CattleBusinessDao
import com.sotraubo.app.core.database.dao.TradeOrderQueryProjection
import com.sotraubo.app.core.database.entity.AttachmentEntity
import com.sotraubo.app.core.database.entity.CashTransactionEntity
import com.sotraubo.app.core.database.entity.ContactEntity
import com.sotraubo.app.core.database.entity.TradeOrderEntity
import com.sotraubo.app.core.model.CashFlowType
import com.sotraubo.app.core.model.CashTransactionWithContact
import com.sotraubo.app.core.model.Contact
import com.sotraubo.app.core.model.ContactWithSummary
import com.sotraubo.app.core.model.DashboardFinancialSummary
import com.sotraubo.app.core.model.DebtHistoryEvent
import com.sotraubo.app.core.model.DebtSortOption
import com.sotraubo.app.core.model.PaymentMethod
import com.sotraubo.app.core.model.PeriodBusinessStats
import com.sotraubo.app.core.model.PersonBidirectionalDebtDetail
import com.sotraubo.app.core.model.PersonDebtSummaryItem
import com.sotraubo.app.core.model.TradeOrderType
import com.sotraubo.app.core.model.TradeOrderWithPayment
import com.sotraubo.app.core.model.UnifiedActivityItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import kotlin.math.max

sealed interface OperationOutcome<out T> {
    data class Success<T>(val data: T) : OperationOutcome<T>
    data class Error(val vietnameseMessage: String) : OperationOutcome<Nothing>
}

interface CattleBusinessRepository {
    fun observeAllContactsWithSummary(): Flow<List<ContactWithSummary>>
    fun observeAllTradeOrders(): Flow<List<TradeOrderWithPayment>>
    fun observeAllCashTransactions(): Flow<List<CashTransactionWithContact>>
    fun observeDashboardSummary(): Flow<DashboardFinancialSummary>
    fun observeReceivableDebtors(searchQuery: String, sortOption: DebtSortOption): Flow<List<PersonDebtSummaryItem>>
    fun observePayableCreditors(searchQuery: String, sortOption: DebtSortOption): Flow<List<PersonDebtSummaryItem>>
    fun observePersonBidirectionalDebtDetail(contactId: Long): Flow<PersonBidirectionalDebtDetail?>
    fun observeContactWithSummary(contactId: Long): Flow<ContactWithSummary?>
    fun observeRecentUnifiedActivities(limit: Int = 10): Flow<List<UnifiedActivityItem>>
    fun observePeriodBusinessStats(
        periodType: ReportPeriodType,
        customStartMillis: Long? = null,
        customEndMillis: Long? = null
    ): Flow<PeriodBusinessStats>

    suspend fun saveContact(
        id: Long = 0L,
        fullName: String,
        phone: String,
        address: String,
        note: String
    ): OperationOutcome<Contact>

    suspend fun deleteContact(contactId: Long): OperationOutcome<Unit>

    suspend fun saveTradeOrder(
        orderId: Long = 0L,
        orderType: TradeOrderType,
        contactId: Long?,
        transactionDate: Long,
        cattleQuantity: Int,
        description: String,
        totalAmount: Long,
        initialPaidAmount: Long,
        initialPaymentMethod: PaymentMethod,
        note: String,
        attachmentPaths: List<String> = emptyList()
    ): OperationOutcome<Long>

    suspend fun deleteTradeOrder(orderId: Long): OperationOutcome<Unit>

    suspend fun saveCashTransaction(
        transactionId: Long = 0L,
        flowType: CashFlowType,
        contactId: Long?,
        linkedTradeOrderId: Long?,
        autoAllocateOldestDebt: Boolean = false,
        amount: Long,
        transactionDate: Long,
        paymentMethod: PaymentMethod,
        shortContent: String,
        note: String,
        attachmentPaths: List<String> = emptyList()
    ): OperationOutcome<Long>

    suspend fun deleteCashTransaction(transactionId: Long): OperationOutcome<Unit>

    suspend fun getTradeOrderById(orderId: Long): TradeOrderWithPayment?
    suspend fun getInitialPaymentForOrder(orderId: Long): CashTransactionWithContact?
    suspend fun getCashTransactionById(transactionId: Long): CashTransactionWithContact?
}

class OfflineCattleBusinessRepository(
    private val database: CattleBusinessDatabase?,
    private val dao: CattleBusinessDao,
    private val dispatchers: AppDispatchers
) : CattleBusinessRepository {

    private suspend fun <R> runInTransaction(block: suspend () -> R): R {
        return if (database != null) {
            database.withTransaction { block() }
        } else {
            block()
        }
    }

    override fun observeAllTradeOrders(): Flow<List<TradeOrderWithPayment>> {
        return combine(
            dao.observeAllTradeOrdersWithPayment(),
            dao.observeAllAttachments()
        ) { orders, attachments ->
            val attachmentMap = attachments
                .mapNotNull { attachment -> attachment.tradeOrderId?.let { it to attachment.localFilePath } }
                .groupBy({ it.first }, { it.second })
            orders.map { it.toDomain(attachmentMap[it.id].orEmpty()) }
        }
    }

    override fun observeAllCashTransactions(): Flow<List<CashTransactionWithContact>> {
        return combine(
            dao.observeAllCashTransactions(),
            dao.observeAllAttachments()
        ) { transactions, attachments ->
            val attachmentMap = attachments
                .mapNotNull { attachment -> attachment.cashTransactionId?.let { it to attachment.localFilePath } }
                .groupBy({ it.first }, { it.second })
            transactions.map { it.toDomain(attachmentMap[it.id].orEmpty()) }
        }
    }

    override fun observeAllContactsWithSummary(): Flow<List<ContactWithSummary>> {
        return combine(
            dao.observeAllContacts(),
            observeAllTradeOrders(),
            observeAllCashTransactions()
        ) { contacts, orders, cashTransactions ->
            val ordersByContact = orders.groupBy { it.contactId }
            val cashByContact = cashTransactions.groupBy { it.contactId }

            contacts.map { entity ->
                val contact = entity.toDomain()
                val personOrders = ordersByContact[contact.id].orEmpty()
                val personCash = cashByContact[contact.id].orEmpty()

                val purchases = personOrders.filter { it.orderType == TradeOrderType.PURCHASE }
                val sales = personOrders.filter { it.orderType == TradeOrderType.SALE }
                val cashIn = personCash.filter { it.flowType == CashFlowType.CASH_IN }
                val cashOut = personCash.filter { it.flowType == CashFlowType.CASH_OUT }

                val latestOrderDate = personOrders.maxOfOrNull { it.transactionDate } ?: 0L
                val latestCashDate = personCash.maxOfOrNull { it.transactionDate } ?: 0L

                ContactWithSummary(
                    contact = contact,
                    totalPurchaseAmount = purchases.sumOf { it.totalAmount },
                    totalSaleAmount = sales.sumOf { it.totalAmount },
                    totalCashInAmount = cashIn.sumOf { it.amount },
                    totalCashOutAmount = cashOut.sumOf { it.amount },
                    receivableDebt = sales.sumOf { it.remainingDebt.coerceAtLeast(0L) },
                    payableDebt = purchases.sumOf { it.remainingDebt.coerceAtLeast(0L) },
                    purchaseOrderCount = purchases.size,
                    saleOrderCount = sales.size,
                    lastTransactionDate = max(latestOrderDate, latestCashDate)
                )
            }
        }
    }

    override fun observeContactWithSummary(contactId: Long): Flow<ContactWithSummary?> {
        return combine(observeAllContactsWithSummary()) { listArray ->
            listArray.first().firstOrNull { it.contact.id == contactId }
        }
    }

    override fun observeDashboardSummary(): Flow<DashboardFinancialSummary> {
        return combine(
            observeAllTradeOrders(),
            observeAllCashTransactions()
        ) { orders, cashTransactions ->
            val totalCashIn = cashTransactions
                .filter { it.flowType == CashFlowType.CASH_IN }
                .sumOf { it.amount }
            val totalCashOut = cashTransactions
                .filter { it.flowType == CashFlowType.CASH_OUT }
                .sumOf { it.amount }
            val standaloneCashIn = cashTransactions
                .filter { it.flowType == CashFlowType.CASH_IN && it.tradeOrderId == null }
                .sumOf { it.amount }
            val standaloneCashOut = cashTransactions
                .filter { it.flowType == CashFlowType.CASH_OUT && it.tradeOrderId == null }
                .sumOf { it.amount }

            val purchases = orders.filter { it.orderType == TradeOrderType.PURCHASE }
            val sales = orders.filter { it.orderType == TradeOrderType.SALE }

            val totalPurchaseAmount = purchases.sumOf { it.totalAmount }
            val totalSaleAmount = sales.sumOf { it.totalAmount }

            val receivableByContact = sales
                .groupBy { it.contactId }
                .mapValues { (_, list) -> list.sumOf { it.remainingDebt.coerceAtLeast(0L) } }
                .filterValues { it > 0L }

            val payableByContact = purchases
                .groupBy { it.contactId }
                .mapValues { (_, list) -> list.sumOf { it.remainingDebt.coerceAtLeast(0L) } }
                .filterValues { it > 0L }

            DashboardFinancialSummary(
                totalCashIn = totalCashIn,
                totalCashOut = totalCashOut,
                totalPurchaseAmount = totalPurchaseAmount,
                totalSaleAmount = totalSaleAmount,
                totalReceivableDebt = receivableByContact.values.sum(),
                totalPayableDebt = payableByContact.values.sum(),
                receivableDebtorCount = receivableByContact.size,
                payableCreditorCount = payableByContact.size,
                standaloneCashIn = standaloneCashIn,
                standaloneCashOut = standaloneCashOut
            )
        }
    }

    override fun observeReceivableDebtors(
        searchQuery: String,
        sortOption: DebtSortOption
    ): Flow<List<PersonDebtSummaryItem>> {
        return combine(
            dao.observeAllContacts(),
            observeAllTradeOrders(),
            observeAllCashTransactions()
        ) { contacts, orders, cashTransactions ->
            val contactMap = contacts.associateBy { it.id }
            val cashByOrder = cashTransactions
                .mapNotNull { tx -> tx.tradeOrderId?.let { it to tx } }
                .groupBy({ it.first }, { it.second })

            val unpaidSalesByContact = orders
                .filter { it.orderType == TradeOrderType.SALE && it.remainingDebt > 0L }
                .groupBy { it.contactId }

            val items = unpaidSalesByContact.mapNotNull { (contactId, unpaidSales) ->
                val contact = contactMap[contactId] ?: return@mapNotNull null
                if (!VietnameseTextNormalizer.matchesQuery(contact.fullName, contact.phone, searchQuery)) {
                    return@mapNotNull null
                }
                val totalDebt = unpaidSales.sumOf { it.remainingDebt }
                if (totalDebt <= 0L) return@mapNotNull null

                val latestDate = unpaidSales.maxOfOrNull { sale ->
                    val latestPaymentDate = cashByOrder[sale.id]?.maxOfOrNull { it.transactionDate } ?: 0L
                    max(sale.transactionDate, latestPaymentDate)
                } ?: 0L

                PersonDebtSummaryItem(
                    contactId = contact.id,
                    fullName = contact.fullName,
                    phone = contact.phone,
                    remainingDebt = totalDebt,
                    relatedOrderCount = unpaidSales.size,
                    lastTransactionDate = latestDate
                )
            }

            when (sortOption) {
                DebtSortOption.AMOUNT_DESC -> items.sortedByDescending { it.remainingDebt }
                DebtSortOption.RECENT_DATE_DESC -> items.sortedByDescending { it.lastTransactionDate }
            }
        }
    }

    override fun observePayableCreditors(
        searchQuery: String,
        sortOption: DebtSortOption
    ): Flow<List<PersonDebtSummaryItem>> {
        return combine(
            dao.observeAllContacts(),
            observeAllTradeOrders(),
            observeAllCashTransactions()
        ) { contacts, orders, cashTransactions ->
            val contactMap = contacts.associateBy { it.id }
            val cashByOrder = cashTransactions
                .mapNotNull { tx -> tx.tradeOrderId?.let { it to tx } }
                .groupBy({ it.first }, { it.second })

            val unpaidPurchasesByContact = orders
                .filter { it.orderType == TradeOrderType.PURCHASE && it.remainingDebt > 0L }
                .groupBy { it.contactId }

            val items = unpaidPurchasesByContact.mapNotNull { (contactId, unpaidPurchases) ->
                val contact = contactMap[contactId] ?: return@mapNotNull null
                if (!VietnameseTextNormalizer.matchesQuery(contact.fullName, contact.phone, searchQuery)) {
                    return@mapNotNull null
                }
                val totalDebt = unpaidPurchases.sumOf { it.remainingDebt }
                if (totalDebt <= 0L) return@mapNotNull null

                val latestDate = unpaidPurchases.maxOfOrNull { purchase ->
                    val latestPaymentDate = cashByOrder[purchase.id]?.maxOfOrNull { it.transactionDate } ?: 0L
                    max(purchase.transactionDate, latestPaymentDate)
                } ?: 0L

                PersonDebtSummaryItem(
                    contactId = contact.id,
                    fullName = contact.fullName,
                    phone = contact.phone,
                    remainingDebt = totalDebt,
                    relatedOrderCount = unpaidPurchases.size,
                    lastTransactionDate = latestDate
                )
            }

            when (sortOption) {
                DebtSortOption.AMOUNT_DESC -> items.sortedByDescending { it.remainingDebt }
                DebtSortOption.RECENT_DATE_DESC -> items.sortedByDescending { it.lastTransactionDate }
            }
        }
    }

    override fun observePersonBidirectionalDebtDetail(contactId: Long): Flow<PersonBidirectionalDebtDetail?> {
        return combine(
            dao.observeContactById(contactId),
            observeAllTradeOrders(),
            observeAllCashTransactions()
        ) { contactEntity, allOrders, allCash ->
            val contact = contactEntity?.toDomain() ?: return@combine null
            val personOrders = allOrders.filter { it.contactId == contactId }
            val personCash = allCash.filter { it.contactId == contactId }

            val saleOrders = personOrders.filter { it.orderType == TradeOrderType.SALE }
            val purchaseOrders = personOrders.filter { it.orderType == TradeOrderType.PURCHASE }

            val saleOrderIds = saleOrders.map { it.id }.toSet()
            val purchaseOrderIds = purchaseOrders.map { it.id }.toSet()

            val linkedCashIn = personCash.filter {
                it.flowType == CashFlowType.CASH_IN && it.tradeOrderId != null && it.tradeOrderId in saleOrderIds
            }
            val linkedCashOut = personCash.filter {
                it.flowType == CashFlowType.CASH_OUT && it.tradeOrderId != null && it.tradeOrderId in purchaseOrderIds
            }
            val standalone = personCash.filter { it.tradeOrderId == null }

            val initialReceivableTotal = saleOrders.sumOf { it.totalAmount }
            val paidReceivableTotal = saleOrders.sumOf { it.paidAmount }
            val currentReceivableDebt = saleOrders.sumOf { it.remainingDebt.coerceAtLeast(0L) }

            val initialPayableTotal = purchaseOrders.sumOf { it.totalAmount }
            val paidPayableTotal = purchaseOrders.sumOf { it.paidAmount }
            val currentPayableDebt = purchaseOrders.sumOf { it.remainingDebt.coerceAtLeast(0L) }

            val timeline = mutableListOf<DebtHistoryEvent>()

            saleOrders.forEach { sale ->
                timeline.add(
                    DebtHistoryEvent(
                        dateMillis = sale.transactionDate,
                        title = "Bán hàng (${sale.cattleQuantity} con)",
                        description = sale.description,
                        amount = sale.totalAmount,
                        isIncreaseDebt = true,
                        isReceivableSide = true
                    )
                )
            }
            linkedCashIn.forEach { cashIn ->
                timeline.add(
                    DebtHistoryEvent(
                        dateMillis = cashIn.transactionDate,
                        title = if (cashIn.isInitialPayment) "Nhận tiền ngay lúc bán" else "Nhận tiền khách trả thêm",
                        description = cashIn.shortContent,
                        amount = cashIn.amount,
                        isIncreaseDebt = false,
                        isReceivableSide = true
                    )
                )
            }
            purchaseOrders.forEach { purchase ->
                timeline.add(
                    DebtHistoryEvent(
                        dateMillis = purchase.transactionDate,
                        title = "Mua hàng (${purchase.cattleQuantity} con)",
                        description = purchase.description,
                        amount = purchase.totalAmount,
                        isIncreaseDebt = true,
                        isReceivableSide = false
                    )
                )
            }
            linkedCashOut.forEach { cashOut ->
                timeline.add(
                    DebtHistoryEvent(
                        dateMillis = cashOut.transactionDate,
                        title = if (cashOut.isInitialPayment) "Trả tiền ngay lúc mua" else "Chuyển tiền trả nợ thêm",
                        description = cashOut.shortContent,
                        amount = cashOut.amount,
                        isIncreaseDebt = false,
                        isReceivableSide = false
                    )
                )
            }

            PersonBidirectionalDebtDetail(
                contact = contact,
                initialReceivableTotal = initialReceivableTotal,
                paidReceivableTotal = paidReceivableTotal,
                currentReceivableDebt = currentReceivableDebt,
                saleOrders = saleOrders,
                linkedCashInTransactions = linkedCashIn,
                initialPayableTotal = initialPayableTotal,
                paidPayableTotal = paidPayableTotal,
                currentPayableDebt = currentPayableDebt,
                purchaseOrders = purchaseOrders,
                linkedCashOutTransactions = linkedCashOut,
                standaloneTransactions = standalone,
                debtTimeline = timeline.sortedByDescending { it.dateMillis }
            )
        }
    }

    override fun observeRecentUnifiedActivities(limit: Int): Flow<List<UnifiedActivityItem>> {
        return combine(
            observeAllTradeOrders(),
            observeAllCashTransactions()
        ) { orders, cashTransactions ->
            val orderActivities = orders.map { order ->
                UnifiedActivityItem(
                    uniqueKey = "ORDER_${order.id}",
                    recordId = order.id,
                    isTradeOrder = true,
                    titleLabel = order.orderType.vietnameseLabel,
                    tradeOrderType = order.orderType,
                    contactId = order.contactId,
                    contactName = order.contactName,
                    amount = order.totalAmount,
                    paidAmount = order.paidAmount,
                    remainingDebt = order.remainingDebt,
                    transactionDate = order.transactionDate,
                    shortDescription = "${order.cattleQuantity} con • ${order.description}",
                    createdAt = order.createdAt
                )
            }

            // Hiển thị các giao dịch Nhận/Chuyển tiền độc lập hoặc trả nợ thêm (tránh lặp dòng với khoản trả ngay lúc tạo đơn)
            val standaloneOrSubsequentCash = cashTransactions
                .filter { !it.isInitialPayment }
                .map { tx ->
                    UnifiedActivityItem(
                        uniqueKey = "CASH_${tx.id}",
                        recordId = tx.id,
                        isTradeOrder = false,
                        titleLabel = tx.flowType.vietnameseLabel,
                        cashFlowType = tx.flowType,
                        contactId = tx.contactId,
                        contactName = tx.contactName,
                        amount = tx.amount,
                        transactionDate = tx.transactionDate,
                        shortDescription = tx.shortContent.ifBlank { tx.tradeOrderDescription ?: tx.flowType.vietnameseLabel },
                        paymentMethodLabel = tx.paymentMethod.vietnameseLabel,
                        createdAt = tx.createdAt
                    )
                }

            (orderActivities + standaloneOrSubsequentCash)
                .sortedWith(compareByDescending<UnifiedActivityItem> { it.transactionDate }.thenByDescending { it.createdAt })
                .take(limit)
        }
    }

    override fun observePeriodBusinessStats(
        periodType: ReportPeriodType,
        customStartMillis: Long?,
        customEndMillis: Long?
    ): Flow<PeriodBusinessStats> {
        return combine(
            observeAllTradeOrders(),
            observeAllCashTransactions()
        ) { orders, cashTransactions ->
            val range = DateFormatter.getPeriodRange(
                periodType = periodType,
                customStartMillis = customStartMillis,
                customEndMillis = customEndMillis
            )
            val ordersInPeriod = orders.filter { it.transactionDate in range.startMillis..range.endMillis }
            val cashInPeriod = cashTransactions.filter { it.transactionDate in range.startMillis..range.endMillis }

            val purchases = ordersInPeriod.filter { it.orderType == TradeOrderType.PURCHASE }
            val sales = ordersInPeriod.filter { it.orderType == TradeOrderType.SALE }
            val cashIn = cashInPeriod.filter { it.flowType == CashFlowType.CASH_IN }
            val cashOut = cashInPeriod.filter { it.flowType == CashFlowType.CASH_OUT }

            PeriodBusinessStats(
                periodLabel = range.label,
                totalPurchaseAmount = purchases.sumOf { it.totalAmount },
                totalSaleAmount = sales.sumOf { it.totalAmount },
                totalCashIn = cashIn.sumOf { it.amount },
                totalCashOut = cashOut.sumOf { it.amount },
                purchaseCattleCount = purchases.sumOf { it.cattleQuantity },
                saleCattleCount = sales.sumOf { it.cattleQuantity }
            )
        }
    }

    override suspend fun saveContact(
        id: Long,
        fullName: String,
        phone: String,
        address: String,
        note: String
    ): OperationOutcome<Contact> = withContext(dispatchers.io) {
        when (val validation = FinancialRuleValidator.validateContactName(fullName)) {
            is ValidationResult.Invalid -> return@withContext OperationOutcome.Error(validation.vietnameseMessage)
            ValidationResult.Valid -> Unit
        }

        val now = System.currentTimeMillis()
        val trimmedName = fullName.trim()
        val trimmedPhone = phone.trim()
        val existing = if (id > 0L) dao.getContactById(id) else null

        val entity = ContactEntity(
            id = id,
            fullName = trimmedName,
            nameNormalized = VietnameseTextNormalizer.normalizeName(trimmedName),
            phone = trimmedPhone,
            phoneNormalized = VietnameseTextNormalizer.normalizePhone(trimmedPhone),
            address = address.trim(),
            note = note.trim(),
            createdAt = existing?.createdAt ?: now,
            updatedAt = now
        )

        val savedId = if (id > 0L) {
            dao.updateContact(entity)
            id
        } else {
            dao.insertContact(entity)
        }

        OperationOutcome.Success(entity.copy(id = savedId).toDomain())
    }

    override suspend fun deleteContact(contactId: Long): OperationOutcome<Unit> = withContext(dispatchers.io) {
        val count = dao.countTransactionsForContact(contactId)
        if (count > 0) {
            return@withContext OperationOutcome.Error(
                "Không thể xóa người liên quan này vì đã có $count giao dịch trong lịch sử. " +
                    "Để bảo toàn sổ sách, vui lòng giữ hồ sơ hoặc xóa các giao dịch liên quan trước."
            )
        }
        dao.deleteContactById(contactId)
        OperationOutcome.Success(Unit)
    }

    override suspend fun saveTradeOrder(
        orderId: Long,
        orderType: TradeOrderType,
        contactId: Long?,
        transactionDate: Long,
        cattleQuantity: Int,
        description: String,
        totalAmount: Long,
        initialPaidAmount: Long,
        initialPaymentMethod: PaymentMethod,
        note: String,
        attachmentPaths: List<String>
    ): OperationOutcome<Long> = withContext(dispatchers.io) {
        val isPurchase = (orderType == TradeOrderType.PURCHASE)
        val existingInitialPayment = if (orderId > 0L) dao.getInitialPaymentForOrder(orderId) else null
        val existingSubsequentPaid = if (orderId > 0L) {
            dao.sumPaidAmountForOrder(orderId, excludeTransactionId = existingInitialPayment?.id)
        } else {
            0L
        }

        val validation = FinancialRuleValidator.validateTradeOrderInput(
            contactId = contactId,
            cattleQuantity = cattleQuantity,
            description = description,
            totalAmount = totalAmount,
            initialPaidAmount = initialPaidAmount,
            existingSubsequentPaidAmount = existingSubsequentPaid,
            isPurchase = isPurchase
        )
        if (validation is ValidationResult.Invalid) {
            return@withContext OperationOutcome.Error(validation.vietnameseMessage)
        }

        val validContactId = contactId
            ?: return@withContext OperationOutcome.Error("Vui lòng chọn đối tác.")
        val now = System.currentTimeMillis()

        val savedOrderId = runInTransaction {
            val existingOrder = if (orderId > 0L) dao.getRawTradeOrderById(orderId) else null
            val orderEntity = TradeOrderEntity(
                id = orderId,
                orderType = orderType.dbValue,
                contactId = validContactId,
                transactionDate = transactionDate,
                cattleQuantity = cattleQuantity,
                description = description.trim(),
                totalAmount = totalAmount,
                note = note.trim(),
                createdAt = existingOrder?.createdAt ?: now,
                updatedAt = now
            )

            val actualOrderId = if (orderId > 0L) {
                dao.updateTradeOrder(orderEntity)
                orderId
            } else {
                dao.insertTradeOrder(orderEntity)
            }

            // Quản lý khoản trả ngay / nhận ngay ban đầu (is_initial_payment = true)
            val flowType = if (isPurchase) CashFlowType.CASH_OUT else CashFlowType.CASH_IN
            val defaultInitialContent = if (isPurchase) {
                "Trả ngay lúc mua: ${description.trim()}"
            } else {
                "Nhận ngay lúc bán: ${description.trim()}"
            }

            if (initialPaidAmount > 0L) {
                if (existingInitialPayment != null) {
                    dao.updateCashTransaction(
                        existingInitialPayment.copy(
                            flowType = flowType.dbValue,
                            contactId = validContactId,
                            amount = initialPaidAmount,
                            transactionDate = transactionDate,
                            paymentMethod = initialPaymentMethod.dbValue,
                            shortContent = defaultInitialContent,
                            updatedAt = now
                        )
                    )
                } else {
                    dao.insertCashTransaction(
                        CashTransactionEntity(
                            flowType = flowType.dbValue,
                            contactId = validContactId,
                            tradeOrderId = actualOrderId,
                            isInitialPayment = true,
                            amount = initialPaidAmount,
                            transactionDate = transactionDate,
                            paymentMethod = initialPaymentMethod.dbValue,
                            shortContent = defaultInitialContent,
                            note = "",
                            createdAt = now,
                            updatedAt = now
                        )
                    )
                }
            } else if (existingInitialPayment != null) {
                dao.deleteCashTransactionById(existingInitialPayment.id)
            }

            dao.deleteAttachmentsForOrder(actualOrderId)
            if (attachmentPaths.isNotEmpty()) {
                dao.insertAttachments(
                    attachmentPaths.map { path ->
                        AttachmentEntity(
                            tradeOrderId = actualOrderId,
                            localFilePath = path,
                            createdAt = now
                        )
                    }
                )
            }

            actualOrderId
        }

        OperationOutcome.Success(savedOrderId)
    }

    override suspend fun deleteTradeOrder(orderId: Long): OperationOutcome<Unit> = withContext(dispatchers.io) {
        runInTransaction {
            dao.deleteTradeOrderById(orderId)
        }
        OperationOutcome.Success(Unit)
    }

    override suspend fun saveCashTransaction(
        transactionId: Long,
        flowType: CashFlowType,
        contactId: Long?,
        linkedTradeOrderId: Long?,
        autoAllocateOldestDebt: Boolean,
        amount: Long,
        transactionDate: Long,
        paymentMethod: PaymentMethod,
        shortContent: String,
        note: String,
        attachmentPaths: List<String>
    ): OperationOutcome<Long> = withContext(dispatchers.io) {
        val isCashIn = (flowType == CashFlowType.CASH_IN)

        if (contactId == null || contactId <= 0L) {
            val role = if (isCashIn) "người đưa tiền" else "người nhận tiền"
            return@withContext OperationOutcome.Error("Vui lòng chọn $role trước khi lưu giao dịch.")
        }
        if (amount <= 0L) {
            val label = if (isCashIn) "Số tiền nhận" else "Số tiền chuyển"
            return@withContext OperationOutcome.Error("$label phải lớn hơn 0 đ.")
        }

        val now = System.currentTimeMillis()

        // Trường hợp tự động phân bổ vào các đơn nợ cũ nhất (khi người dùng chọn trả gộp nhiều đơn)
        if (autoAllocateOldestDebt && transactionId == 0L && linkedTradeOrderId == null) {
            return@withContext allocatePaymentToOldestOrders(
                flowType = flowType,
                contactId = contactId,
                totalPaymentAmount = amount,
                transactionDate = transactionDate,
                paymentMethod = paymentMethod,
                shortContent = shortContent,
                note = note,
                attachmentPaths = attachmentPaths,
                now = now
            )
        }

        var linkedOrderTotal: Long? = null
        var linkedOrderPaidExcludingCurrent: Long? = null
        var linkedOrderDate: Long? = null
        var defaultContentFromOrder = ""

        if (linkedTradeOrderId != null) {
            val orderProjection = dao.getTradeOrderWithPaymentById(linkedTradeOrderId)
                ?: return@withContext OperationOutcome.Error("Không tìm thấy đơn mua/bán được liên kết.")
            linkedOrderTotal = orderProjection.totalAmount
            linkedOrderPaidExcludingCurrent = dao.sumPaidAmountForOrder(
                orderId = linkedTradeOrderId,
                excludeTransactionId = if (transactionId > 0L) transactionId else null
            )
            linkedOrderDate = orderProjection.transactionDate
            defaultContentFromOrder = if (isCashIn) {
                "Thu tiền nợ đơn bán (${orderProjection.description})"
            } else {
                "Trả tiền nợ đơn mua (${orderProjection.description})"
            }
        }

        val validation = FinancialRuleValidator.validateCashTransactionInput(
            contactId = contactId,
            amount = amount,
            linkedOrderTotalAmount = linkedOrderTotal,
            linkedOrderAlreadyPaidExcludingCurrent = linkedOrderPaidExcludingCurrent,
            linkedOrderDateMillis = linkedOrderDate,
            isCashIn = isCashIn
        )
        if (validation is ValidationResult.Invalid) {
            return@withContext OperationOutcome.Error(validation.vietnameseMessage)
        }

        val finalShortContent = shortContent.trim().ifEmpty {
            defaultContentFromOrder.ifEmpty { flowType.vietnameseLabel }
        }

        val savedId = runInTransaction {
            val existing = if (transactionId > 0L) dao.getRawCashTransactionById(transactionId) else null
            val entity = CashTransactionEntity(
                id = transactionId,
                flowType = flowType.dbValue,
                contactId = contactId,
                tradeOrderId = linkedTradeOrderId,
                isInitialPayment = existing?.isInitialPayment ?: false,
                amount = amount,
                transactionDate = transactionDate,
                paymentMethod = paymentMethod.dbValue,
                shortContent = finalShortContent,
                note = note.trim(),
                createdAt = existing?.createdAt ?: now,
                updatedAt = now
            )

            val actualId = if (transactionId > 0L) {
                dao.updateCashTransaction(entity)
                transactionId
            } else {
                dao.insertCashTransaction(entity)
            }

            dao.deleteAttachmentsForCashTransaction(actualId)
            if (attachmentPaths.isNotEmpty()) {
                dao.insertAttachments(
                    attachmentPaths.map { path ->
                        AttachmentEntity(
                            cashTransactionId = actualId,
                            localFilePath = path,
                            createdAt = now
                        )
                    }
                )
            }
            actualId
        }

        OperationOutcome.Success(savedId)
    }

    private suspend fun allocatePaymentToOldestOrders(
        flowType: CashFlowType,
        contactId: Long,
        totalPaymentAmount: Long,
        transactionDate: Long,
        paymentMethod: PaymentMethod,
        shortContent: String,
        note: String,
        attachmentPaths: List<String>,
        now: Long
    ): OperationOutcome<Long> {
        val targetOrderType = if (flowType == CashFlowType.CASH_IN) TradeOrderType.SALE else TradeOrderType.PURCHASE
        val allOrders = dao.getAllTradeOrdersSnapshot()
            .filter { it.contactId == contactId && it.orderType == targetOrderType.dbValue }
            .sortedWith(compareBy<TradeOrderEntity> { it.transactionDate }.thenBy { it.id })

        val unpaidOrdersWithRemaining = mutableListOf<Pair<TradeOrderEntity, Long>>()
        for (order in allOrders) {
            val paid = dao.sumPaidAmountForOrder(order.id)
            val remaining = order.totalAmount - paid
            if (remaining > 0L) {
                unpaidOrdersWithRemaining.add(order to remaining)
            }
        }

        val totalRemainingDebt = unpaidOrdersWithRemaining.sumOf { it.second }
        if (totalPaymentAmount > totalRemainingDebt) {
            val label = if (flowType == CashFlowType.CASH_IN) "Tổng nợ phải thu" else "Tổng nợ phải trả"
            return OperationOutcome.Error(
                "$label của người này chỉ còn ${com.sotraubo.app.core.common.format.CurrencyFormatter.formatVnd(totalRemainingDebt)}. " +
                    "Không thể trả nợ vượt số dư còn nợ."
            )
        }

        val lastInsertedId = runInTransaction {
            var remainingToAllocate = totalPaymentAmount
            var lastId = 0L
            for ((order, orderRemaining) in unpaidOrdersWithRemaining) {
                if (remainingToAllocate <= 0L) break
                val allocated = minOf(remainingToAllocate, orderRemaining)
                val content = shortContent.trim().ifEmpty {
                    if (flowType == CashFlowType.CASH_IN) {
                        "Thu tiền nợ đơn bán (${order.description})"
                    } else {
                        "Trả tiền nợ đơn mua (${order.description})"
                    }
                }
                lastId = dao.insertCashTransaction(
                    CashTransactionEntity(
                        flowType = flowType.dbValue,
                        contactId = contactId,
                        tradeOrderId = order.id,
                        isInitialPayment = false,
                        amount = allocated,
                        transactionDate = transactionDate,
                        paymentMethod = paymentMethod.dbValue,
                        shortContent = content,
                        note = note.trim(),
                        createdAt = now,
                        updatedAt = now
                    )
                )
                if (attachmentPaths.isNotEmpty()) {
                    dao.insertAttachments(
                        attachmentPaths.map { path ->
                            AttachmentEntity(
                                cashTransactionId = lastId,
                                localFilePath = path,
                                createdAt = now
                            )
                        }
                    )
                }
                remainingToAllocate -= allocated
            }
            lastId
        }

        return OperationOutcome.Success(lastInsertedId)
    }

    override suspend fun deleteCashTransaction(transactionId: Long): OperationOutcome<Unit> =
        withContext(dispatchers.io) {
            runInTransaction {
                dao.deleteCashTransactionById(transactionId)
            }
            OperationOutcome.Success(Unit)
        }

    override suspend fun getTradeOrderById(orderId: Long): TradeOrderWithPayment? =
        withContext(dispatchers.io) {
            val projection = dao.getTradeOrderWithPaymentById(orderId) ?: return@withContext null
            val paths = dao.getAllAttachmentsSnapshot()
                .filter { it.tradeOrderId == orderId }
                .map { it.localFilePath }
            projection.toDomain(paths)
        }

    override suspend fun getInitialPaymentForOrder(orderId: Long): CashTransactionWithContact? =
        withContext(dispatchers.io) {
            val initial = dao.getInitialPaymentForOrder(orderId) ?: return@withContext null
            getCashTransactionById(initial.id)
        }

    override suspend fun getCashTransactionById(transactionId: Long): CashTransactionWithContact? =
        withContext(dispatchers.io) {
            val raw = dao.getRawCashTransactionById(transactionId) ?: return@withContext null
            val contact = dao.getContactById(raw.contactId)
            val order = raw.tradeOrderId?.let { dao.getRawTradeOrderById(it) }
            val paths = dao.getAllAttachmentsSnapshot()
                .filter { it.cashTransactionId == transactionId }
                .map { it.localFilePath }
            CashTransactionWithContact(
                id = raw.id,
                flowType = CashFlowType.fromDb(raw.flowType),
                contactId = raw.contactId,
                contactName = contact?.fullName.orEmpty(),
                contactPhone = contact?.phone.orEmpty(),
                tradeOrderId = raw.tradeOrderId,
                tradeOrderDescription = order?.description,
                isInitialPayment = raw.isInitialPayment,
                amount = raw.amount,
                transactionDate = raw.transactionDate,
                paymentMethod = PaymentMethod.fromDb(raw.paymentMethod),
                shortContent = raw.shortContent,
                note = raw.note,
                attachmentPaths = paths,
                createdAt = raw.createdAt
            )
        }

    private fun ContactEntity.toDomain(): Contact = Contact(
        id = id,
        fullName = fullName,
        nameNormalized = nameNormalized,
        phone = phone,
        phoneNormalized = phoneNormalized,
        address = address,
        note = note,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun TradeOrderQueryProjection.toDomain(attachmentPaths: List<String>): TradeOrderWithPayment =
        TradeOrderWithPayment(
            id = id,
            orderType = TradeOrderType.fromDb(orderType),
            contactId = contactId,
            contactName = contactName,
            contactPhone = contactPhone,
            transactionDate = transactionDate,
            cattleQuantity = cattleQuantity,
            description = description,
            totalAmount = totalAmount,
            paidAmount = paidAmount,
            remainingDebt = remainingDebt,
            note = note,
            attachmentPaths = attachmentPaths,
            createdAt = createdAt
        )

    private fun CashTransactionQueryProjection.toDomain(attachmentPaths: List<String>): CashTransactionWithContact =
        CashTransactionWithContact(
            id = id,
            flowType = CashFlowType.fromDb(flowType),
            contactId = contactId,
            contactName = contactName,
            contactPhone = contactPhone,
            tradeOrderId = tradeOrderId,
            tradeOrderDescription = tradeOrderDescription,
            isInitialPayment = isInitialPayment,
            amount = amount,
            transactionDate = transactionDate,
            paymentMethod = PaymentMethod.fromDb(paymentMethod),
            shortContent = shortContent,
            note = note,
            attachmentPaths = attachmentPaths,
            createdAt = createdAt
        )
}
