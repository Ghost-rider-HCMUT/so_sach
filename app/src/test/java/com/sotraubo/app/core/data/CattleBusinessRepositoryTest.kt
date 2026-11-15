package com.sotraubo.app.core.data

import com.sotraubo.app.core.common.dispatcher.AppDispatchers
import com.sotraubo.app.core.common.format.ReportPeriodType
import com.sotraubo.app.core.data.repository.OfflineCattleBusinessRepository
import com.sotraubo.app.core. data.repository.OperationOutcome
import com.sotraubo.app.core.database.dao.CashTransactionQueryProjection
import com.sotraubo.app.core.database.dao.CattleBusinessDao
import com.sotraubo.app.core.database.dao.TradeOrderQueryProjection
import com.sotraubo.app.core.database.entity.AttachmentEntity
import com.sotraubo.app.core.database.entity.CashTransactionEntity
import com.sotraubo.app.core.database.entity.ContactEntity
import com.sotraubo.app.core.database.entity.TradeOrderEntity
import com.sotraubo.app.core.model.CashFlowType
import com.sotraubo.app.core.model.DebtSortOption
import com.sotraubo.app.core.model.PaymentMethod
import com.sotraubo.app.core.model.TradeOrderType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Fake In-Memory DAO mô phỏng chính xác 100% hành vi của SQLite/Room (Bao gồm ForeignKey CASCADE)
 * để chạy kiểm thử tức thì toàn bộ 14 kịch bản nghiệp vụ trong 11_TEST_CASES.md.
 */
class InMemoryCattleBusinessDao : CattleBusinessDao {
    private var nextContactId = 1L
    private var nextOrderId = 1L
    private var nextCashId = 1L
    private var nextAttachmentId = 1L

    private val contactsFlow = MutableStateFlow<List<ContactEntity>>(emptyList())
    private val ordersFlow = MutableStateFlow<List<TradeOrderEntity>>(emptyList())
    private val cashFlow = MutableStateFlow<List<CashTransactionEntity>>(emptyList())
    private val attachmentsFlow = MutableStateFlow<List<AttachmentEntity>>(emptyList())

    override fun observeAllContacts(): Flow<List<ContactEntity>> =
        contactsFlow.map { list -> list.sortedBy { it.fullName } }

    override fun observeContactById(contactId: Long): Flow<ContactEntity?> =
        contactsFlow.map { list -> list.firstOrNull { it.id == contactId } }

    override suspend fun getContactById(contactId: Long): ContactEntity? =
        contactsFlow.value.firstOrNull { it.id == contactId }

    override suspend fun getAllContactsSnapshot(): List<ContactEntity> =
        contactsFlow.value.sortedBy { it.id }

    override suspend fun insertContact(contact: ContactEntity): Long {
        val id = if (contact.id > 0L) contact.id else nextContactId++
        if (id >= nextContactId) nextContactId = id + 1
        val saved = contact.copy(id = id)
        contactsFlow.value = contactsFlow.value.filterNot { it.id == id } + saved
        return id
    }

    override suspend fun updateContact(contact: ContactEntity) {
        contactsFlow.value = contactsFlow.value.map { if (it.id == contact.id) contact else it }
    }

    override suspend fun deleteContactById(contactId: Long) {
        contactsFlow.value = contactsFlow.value.filterNot { it.id == contactId }
    }

    override suspend fun countTransactionsForContact(contactId: Long): Int {
        val orderCount = ordersFlow.value.count { it.contactId == contactId }
        val cashCount = cashFlow.value.count { it.contactId == contactId }
        return orderCount + cashCount
    }

    override fun observeAllTradeOrdersWithPayment(): Flow<List<TradeOrderQueryProjection>> {
        return combine(contactsFlow, ordersFlow, cashFlow) { contacts, orders, cash ->
            val contactMap = contacts.associateBy { it.id }
            val cashByOrder = cash.filter { it.tradeOrderId != null }.groupBy { it.tradeOrderId!! }
            orders.mapNotNull { o ->
                val c = contactMap[o.contactId] ?: return@mapNotNull null
                val paid = cashByOrder[o.id]?.sumOf { it.amount } ?: 0L
                TradeOrderQueryProjection(
                    id = o.id,
                    orderType = o.orderType,
                    contactId = o.contactId,
                    contactName = c.fullName,
                    contactPhone = c.phone,
                    transactionDate = o.transactionDate,
                    cattleQuantity = o.cattleQuantity,
                    description = o.description,
                    totalAmount = o.totalAmount,
                    paidAmount = paid,
                    remainingDebt = o.totalAmount - paid,
                    note = o.note,
                    createdAt = o.createdAt
                )
            }.sortedWith(compareByDescending<TradeOrderQueryProjection> { it.transactionDate }.thenByDescending { it.id })
        }
    }

    override suspend fun getTradeOrderWithPaymentById(orderId: Long): TradeOrderQueryProjection? {
        return observeAllTradeOrdersWithPayment().first().firstOrNull { it.id == orderId }
    }

    override suspend fun getRawTradeOrderById(orderId: Long): TradeOrderEntity? =
        ordersFlow.value.firstOrNull { it.id == orderId }

    override suspend fun getAllTradeOrdersSnapshot(): List<TradeOrderEntity> =
        ordersFlow.value.sortedBy { it.id }

    override suspend fun insertTradeOrder(order: TradeOrderEntity): Long {
        val id = if (order.id > 0L) order.id else nextOrderId++
        if (id >= nextOrderId) nextOrderId = id + 1
        val saved = order.copy(id = id)
        ordersFlow.value = ordersFlow.value.filterNot { it.id == id } + saved
        return id
    }

    override suspend fun updateTradeOrder(order: TradeOrderEntity) {
        ordersFlow.value = ordersFlow.value.map { if (it.id == order.id) order else it }
    }

    override suspend fun deleteTradeOrderById(orderId: Long) {
        ordersFlow.value = ordersFlow.value.filterNot { it.id == orderId }
        // Mô phỏng ON DELETE CASCADE cho cash_transactions và attachments
        cashFlow.value = cashFlow.value.filterNot { it.tradeOrderId == orderId }
        attachmentsFlow.value = attachmentsFlow.value.filterNot { it.tradeOrderId == orderId }
    }

    override fun observeAllCashTransactions(): Flow<List<CashTransactionQueryProjection>> {
        return combine(contactsFlow, ordersFlow, cashFlow) { contacts, orders, cash ->
            val contactMap = contacts.associateBy { it.id }
            val orderMap = orders.associateBy { it.id }
            cash.mapNotNull { ct ->
                val c = contactMap[ct.contactId] ?: return@mapNotNull null
                val o = ct.tradeOrderId?.let { orderMap[it] }
                CashTransactionQueryProjection(
                    id = ct.id,
                    flowType = ct.flowType,
                    contactId = ct.contactId,
                    contactName = c.fullName,
                    contactPhone = c.phone,
                    tradeOrderId = ct.tradeOrderId,
                    tradeOrderDescription = o?.description,
                    isInitialPayment = ct.isInitialPayment,
                    amount = ct.amount,
                    transactionDate = ct.transactionDate,
                    paymentMethod = ct.paymentMethod,
                    shortContent = ct.shortContent,
                    note = ct.note,
                    createdAt = ct.createdAt
                )
            }.sortedWith(compareByDescending<CashTransactionQueryProjection> { it.transactionDate }.thenByDescending { it.id })
        }
    }

    override suspend fun getRawCashTransactionById(transactionId: Long): CashTransactionEntity? =
        cashFlow.value.firstOrNull { it.id == transactionId }

    override suspend fun getInitialPaymentForOrder(orderId: Long): CashTransactionEntity? =
        cashFlow.value.firstOrNull { it.tradeOrderId == orderId && it.isInitialPayment }

    override suspend fun sumPaidAmountForOrder(orderId: Long, excludeTransactionId: Long?): Long {
        return cashFlow.value
            .filter { it.tradeOrderId == orderId && (excludeTransactionId == null || it.id != excludeTransactionId) }
            .sumOf { it.amount }
    }

    override suspend fun getAllCashTransactionsSnapshot(): List<CashTransactionEntity> =
        cashFlow.value.sortedBy { it.id }

    override suspend fun insertCashTransaction(transaction: CashTransactionEntity): Long {
        val id = if (transaction.id > 0L) transaction.id else nextCashId++
        if (id >= nextCashId) nextCashId = id + 1
        val saved = transaction.copy(id = id)
        cashFlow.value = cashFlow.value.filterNot { it.id == id } + saved
        return id
    }

    override suspend fun updateCashTransaction(transaction: CashTransactionEntity) {
        cashFlow.value = cashFlow.value.map { if (it.id == transaction.id) transaction else it }
    }

    override suspend fun deleteCashTransactionById(transactionId: Long) {
        cashFlow.value = cashFlow.value.filterNot { it.id == transactionId }
        attachmentsFlow.value = attachmentsFlow.value.filterNot { it.cashTransactionId == transactionId }
    }

    override fun observeAllAttachments(): Flow<List<AttachmentEntity>> = attachmentsFlow

    override suspend fun getAllAttachmentsSnapshot(): List<AttachmentEntity> = attachmentsFlow.value

    override suspend fun insertAttachments(attachments: List<AttachmentEntity>) {
        val withIds = attachments.map {
            val id = if (it.id > 0L) it.id else nextAttachmentId++
            if (id >= nextAttachmentId) nextAttachmentId = id + 1
            it.copy(id = id)
        }
        attachmentsFlow.value = attachmentsFlow.value + withIds
    }

    override suspend fun deleteAttachmentsForOrder(orderId: Long) {
        attachmentsFlow.value = attachmentsFlow.value.filterNot { it.tradeOrderId == orderId }
    }

    override suspend fun deleteAttachmentsForCashTransaction(transactionId: Long) {
        attachmentsFlow.value = attachmentsFlow.value.filterNot { it.cashTransactionId == transactionId }
    }

    override suspend fun clearAllAttachments() {
        attachmentsFlow.value = emptyList()
    }

    override suspend fun clearAllCashTransactions() {
        cashFlow.value = emptyList()
    }

    override suspend fun clearAllTradeOrders() {
        ordersFlow.value = emptyList()
    }

    override suspend fun clearAllContacts() {
        contactsFlow.value = emptyList()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CattleBusinessRepositoryTest {

    private lateinit var dao: InMemoryCattleBusinessDao
    private lateinit var repository: OfflineCattleBusinessRepository
    private val testDispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        dao = InMemoryCattleBusinessDao()
        val dispatchers = object : AppDispatchers {
            override val io: CoroutineDispatcher = testDispatcher
            override val default: CoroutineDispatcher = testDispatcher
            override val main: CoroutineDispatcher = testDispatcher
        }
        repository = OfflineCattleBusinessRepository(
            database = null,
            dao = dao,
            dispatchers = dispatchers
        )
    }

    @Test
    fun `TC-01 and TC-02 Purchase with partial payment and subsequent payment updates debt accurately`() = runTest(testDispatcher) {
        // Tạo người bán Phạm Văn D
        val contactResult = repository.saveContact(
            fullName = "Phạm Văn D",
            phone = "0977 445 566",
            address = "Ba Vì",
            note = ""
        ) as OperationOutcome.Success
        val sellerId = contactResult.data.id

        // TC-01: Mua bò 30 triệu, trả ngay 20 triệu -> Còn nợ 10 triệu
        val orderOutcome = repository.saveTradeOrder(
            orderType = TradeOrderType.PURCHASE,
            contactId = sellerId,
            transactionDate = 1000L,
            cattleQuantity = 2,
            description = "2 bò đực lai Sind",
            totalAmount = 30_000_000L,
            initialPaidAmount = 20_000_000L,
            initialPaymentMethod = PaymentMethod.CASH,
            note = ""
        ) as OperationOutcome.Success
        val purchaseOrderId = orderOutcome.data

        val orderAfterBuy = repository.getTradeOrderById(purchaseOrderId)!!
        assertEquals(30_000_000L, orderAfterBuy.totalAmount)
        assertEquals(20_000_000L, orderAfterBuy.paidAmount)
        assertEquals(10_000_000L, orderAfterBuy.remainingDebt)

        val dash1 = repository.observeDashboardSummary().first()
        assertEquals(30_000_000L, dash1.totalPurchaseAmount)
        assertEquals(20_000_000L, dash1.totalCashOut)
        assertEquals(10_000_000L, dash1.totalPayableDebt)

        // TC-02: Chuyển thêm 5 triệu liên kết với đơn mua trên -> Tổng mua vẫn 30tr, Đã trả 25tr, Còn nợ 5tr
        val payMoreOutcome = repository.saveCashTransaction(
            flowType = CashFlowType.CASH_OUT,
            contactId = sellerId,
            linkedTradeOrderId = purchaseOrderId,
            amount = 5_000_000L,
            transactionDate = 2000L,
            paymentMethod = PaymentMethod.BANK_TRANSFER,
            shortContent = "Trả thêm tiền mua bò",
            note = ""
        )
        assertTrue(payMoreOutcome is OperationOutcome.Success)

        val orderAfterPayMore = repository.getTradeOrderById(purchaseOrderId)!!
        assertEquals(30_000_000L, orderAfterPayMore.totalAmount)
        assertEquals(25_000_000L, orderAfterPayMore.paidAmount)
        assertEquals(5_000_000L, orderAfterPayMore.remainingDebt)

        val dash2 = repository.observeDashboardSummary().first()
        assertEquals(30_000_000L, dash2.totalPurchaseAmount) // Không tạo đơn mua mới
        assertEquals(25_000_000L, dash2.totalCashOut)
        assertEquals(5_000_000L, dash2.totalPayableDebt)
    }

    @Test
    fun `TC-03 and TC-04 Sale with partial receipt and subsequent receipt updates receivable debt`() = runTest(testDispatcher) {
        val buyer = (repository.saveContact(
            fullName = "Nguyễn Văn A",
            phone = "0912 345 678",
            address = "Đan Phượng",
            note = ""
        ) as OperationOutcome.Success).data

        // TC-03: Bán bò 50 triệu, nhận ngay 30 triệu -> Khách còn nợ 20 triệu
        val saleId = (repository.saveTradeOrder(
            orderType = TradeOrderType.SALE,
            contactId = buyer.id,
            transactionDate = 1000L,
            cattleQuantity = 2,
            description = "2 bò thịt BBB",
            totalAmount = 50_000_000L,
            initialPaidAmount = 30_000_000L,
            initialPaymentMethod = PaymentMethod.BANK_TRANSFER,
            note = ""
        ) as OperationOutcome.Success).data

        val sale1 = repository.getTradeOrderById(saleId)!!
        assertEquals(50_000_000L, sale1.totalAmount)
        assertEquals(30_000_000L, sale1.paidAmount)
        assertEquals(20_000_000L, sale1.remainingDebt)

        // TC-04: Khách trả thêm 10 triệu -> Tổng bán vẫn 50tr, Đã nhận 40tr, Khách còn nợ 10tr
        repository.saveCashTransaction(
            flowType = CashFlowType.CASH_IN,
            contactId = buyer.id,
            linkedTradeOrderId = saleId,
            amount = 10_000_000L,
            transactionDate = 2000L,
            paymentMethod = PaymentMethod.CASH,
            shortContent = "Khách trả thêm",
            note = ""
        )

        val sale2 = repository.getTradeOrderById(saleId)!!
        assertEquals(50_000_000L, sale2.totalAmount)
        assertEquals(40_000_000L, sale2.paidAmount)
        assertEquals(10_000_000L, sale2.remainingDebt)

        val dash = repository.observeDashboardSummary().first()
        assertEquals(50_000_000L, dash.totalSaleAmount)
        assertEquals(40_000_000L, dash.totalCashIn)
        assertEquals(10_000_000L, dash.totalReceivableDebt)
    }

    @Test
    fun `TC-05 Bidirectional debt for a single person is kept strictly separate without offsetting`() = runTest(testDispatcher) {
        val partner = (repository.saveContact(
            fullName = "Nguyễn Văn A",
            phone = "0912 345 678",
            address = "",
            note = ""
        ) as OperationOutcome.Success).data

        // Bán cho Nguyễn Văn A 50tr, nhận 30tr -> Nguyễn Văn A nợ mình 20tr
        repository.saveTradeOrder(
            orderType = TradeOrderType.SALE,
            contactId = partner.id,
            transactionDate = 1000L,
            cattleQuantity = 2,
            description = "Bán 2 bò BBB",
            totalAmount = 50_000_000L,
            initialPaidAmount = 30_000_000L,
            initialPaymentMethod = PaymentMethod.CASH,
            note = ""
        )

        // Mua của Nguyễn Văn A 35tr, trả 20tr -> Mình nợ Nguyễn Văn A 15tr
        repository.saveTradeOrder(
            orderType = TradeOrderType.PURCHASE,
            contactId = partner.id,
            transactionDate = 2000L,
            cattleQuantity = 1,
            description = "Mua 1 trâu đực",
            totalAmount = 35_000_000L,
            initialPaidAmount = 20_000_000L,
            initialPaymentMethod = PaymentMethod.CASH,
            note = ""
        )

        val detail = repository.observePersonBidirectionalDebtDetail(partner.id).first()
        assertNotNull(detail)
        // Không tự động bù trừ (20tr và 15tr phải tách biệt)
        assertEquals(20_000_000L, detail!!.currentReceivableDebt)
        assertEquals(15_000_000L, detail.currentPayableDebt)

        val receivables = repository.observeReceivableDebtors("nguyen van a", DebtSortOption.AMOUNT_DESC).first()
        val payables = repository.observePayableCreditors("nguyen van a", DebtSortOption.AMOUNT_DESC).first()
        assertEquals(1, receivables.size)
        assertEquals(20_000_000L, receivables.first().remainingDebt)
        assertEquals(1, payables.size)
        assertEquals(15_000_000L, payables.first().remainingDebt)
    }

    @Test
    fun `TC-06 TC-07 TC-11 TC-12 TC-14 Standalone cash, full settlement, edit and delete recalculation and overpayment block`() = runTest(testDispatcher) {
        val contact = (repository.saveContact(
            fullName = "Trần Văn B",
            phone = "0988 112 233",
            address = "",
            note = ""
        ) as OperationOutcome.Success).data

        // Bán bò 25tr, nhận 15tr -> Còn nợ 10tr
        val saleId = (repository.saveTradeOrder(
            orderType = TradeOrderType.SALE,
            contactId = contact.id,
            transactionDate = 1000L,
            cattleQuantity = 1,
            description = "1 bò vàng",
            totalAmount = 25_000_000L,
            initialPaidAmount = 15_000_000L,
            initialPaymentMethod = PaymentMethod.CASH,
            note = ""
        ) as OperationOutcome.Success).data

        // TC-14: Thử thu vượt số dư còn nợ (nhập 12tr khi chỉ nợ 10tr) -> Bị chặn
        val overpayOutcome = repository.saveCashTransaction(
            flowType = CashFlowType.CASH_IN,
            contactId = contact.id,
            linkedTradeOrderId = saleId,
            amount = 12_000_000L,
            transactionDate = 1500L,
            paymentMethod = PaymentMethod.CASH,
            shortContent = "",
            note = ""
        )
        assertTrue(overpayOutcome is OperationOutcome.Error)

        // TC-06: Nhận tiền độc lập 2tr -> Tổng tiền nhận tăng, nhưng nợ đơn bán vẫn giữ nguyên 10tr
        repository.saveCashTransaction(
            flowType = CashFlowType.CASH_IN,
            contactId = contact.id,
            linkedTradeOrderId = null,
            amount = 2_000_000L,
            transactionDate = 1600L,
            paymentMethod = PaymentMethod.CASH,
            shortContent = "Tiền gửi nhờ",
            note = ""
        )
        assertEquals(10_000_000L, repository.getTradeOrderById(saleId)!!.remainingDebt)
        assertEquals(17_000_000L, repository.observeDashboardSummary().first().totalCashIn)

        // TC-11: Khách trả hết 10tr còn lại -> Nợ về 0, biến mất khỏi danh sách đang nợ mặc định nhưng vẫn còn trong Danh bạ
        val finalPayId = (repository.saveCashTransaction(
            flowType = CashFlowType.CASH_IN,
            contactId = contact.id,
            linkedTradeOrderId = saleId,
            amount = 10_000_000L,
            transactionDate = 2000L,
            paymentMethod = PaymentMethod.BANK_TRANSFER,
            shortContent = "Tất toán hết nợ",
            note = ""
        ) as OperationOutcome.Success).data

        assertEquals(0L, repository.getTradeOrderById(saleId)!!.remainingDebt)
        assertTrue(repository.observeReceivableDebtors("", DebtSortOption.AMOUNT_DESC).first().isEmpty())
        assertEquals(1, repository.observeAllContactsWithSummary().first().size)

        // TC-12: Sửa khoản trả 10tr thành 4tr -> Nợ tự động tăng lại lên 6tr và xuất hiện lại trong danh sách nợ
        repository.saveCashTransaction(
            transactionId = finalPayId,
            flowType = CashFlowType.CASH_IN,
            contactId = contact.id,
            linkedTradeOrderId = saleId,
            amount = 4_000_000L,
            transactionDate = 2000L,
            paymentMethod = PaymentMethod.BANK_TRANSFER,
            shortContent = "Sửa lại số tiền nhận",
            note = ""
        )
        assertEquals(6_000_000L, repository.getTradeOrderById(saleId)!!.remainingDebt)
        assertEquals(1, repository.observeReceivableDebtors("", DebtSortOption.AMOUNT_DESC).first().size)

        // TC-12 (tiếp): Xóa khoản trả 4tr -> Nợ quay về 10tr
        repository.deleteCashTransaction(finalPayId)
        assertEquals(10_000_000L, repository.getTradeOrderById(saleId)!!.remainingDebt)
    }
}
