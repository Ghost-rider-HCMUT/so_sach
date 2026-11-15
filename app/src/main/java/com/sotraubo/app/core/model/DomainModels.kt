package com.sotraubo.app.core.model

import androidx.compose.runtime.Immutable

enum class TradeOrderType(val dbValue: String, val vietnameseLabel: String) {
    PURCHASE("PURCHASE", "Mua hàng"),
    SALE("SALE", "Bán hàng");

    companion object {
        fun fromDb(value: String): TradeOrderType =
            entries.firstOrNull { it.dbValue == value } ?: PURCHASE
    }
}

enum class CashFlowType(val dbValue: String, val vietnameseLabel: String) {
    CASH_IN("CASH_IN", "Nhận tiền"),
    CASH_OUT("CASH_OUT", "Chuyển tiền");

    companion object {
        fun fromDb(value: String): CashFlowType =
            entries.firstOrNull { it.dbValue == value } ?: CASH_IN
    }
}

enum class PaymentMethod(val dbValue: String, val vietnameseLabel: String) {
    CASH("CASH", "Tiền mặt"),
    BANK_TRANSFER("BANK_TRANSFER", "Chuyển khoản");

    companion object {
        fun fromDb(value: String): PaymentMethod =
            entries.firstOrNull { it.dbValue == value } ?: CASH
    }
}

enum class DebtSortOption(val vietnameseLabel: String) {
    AMOUNT_DESC("Số tiền nợ: Cao đến thấp"),
    RECENT_DATE_DESC("Thời gian: Giao dịch gần nhất")
}

@Immutable
data class Contact(
    val id: Long = 0L,
    val fullName: String,
    val nameNormalized: String,
    val phone: String = "",
    val phoneNormalized: String = "",
    val address: String = "",
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Immutable
data class ContactWithSummary(
    val contact: Contact,
    val totalPurchaseAmount: Long = 0L,
    val totalSaleAmount: Long = 0L,
    val totalCashInAmount: Long = 0L,
    val totalCashOutAmount: Long = 0L,
    val receivableDebt: Long = 0L, // Người đó còn nợ mình
    val payableDebt: Long = 0L,    // Mình còn nợ người đó
    val purchaseOrderCount: Int = 0,
    val saleOrderCount: Int = 0,
    val lastTransactionDate: Long = 0L
)

@Immutable
data class TradeOrderWithPayment(
    val id: Long,
    val orderType: TradeOrderType,
    val contactId: Long,
    val contactName: String,
    val contactPhone: String,
    val transactionDate: Long,
    val cattleQuantity: Int,
    val description: String,
    val totalAmount: Long,
    val paidAmount: Long,
    val remainingDebt: Long,
    val note: String,
    val attachmentPaths: List<String> = emptyList(),
    val createdAt: Long
) {
    val isFullyPaid: Boolean
        get() = remainingDebt <= 0L
}

@Immutable
data class CashTransactionWithContact(
    val id: Long,
    val flowType: CashFlowType,
    val contactId: Long,
    val contactName: String,
    val contactPhone: String,
    val tradeOrderId: Long?,
    val tradeOrderDescription: String?,
    val isInitialPayment: Boolean,
    val amount: Long,
    val transactionDate: Long,
    val paymentMethod: PaymentMethod,
    val shortContent: String,
    val note: String,
    val attachmentPaths: List<String> = emptyList(),
    val createdAt: Long
)

@Immutable
data class UnifiedActivityItem(
    val uniqueKey: String,
    val recordId: Long,
    val isTradeOrder: Boolean,
    val titleLabel: String, // "Nhận tiền" | "Chuyển tiền" | "Mua hàng" | "Bán hàng"
    val tradeOrderType: TradeOrderType? = null,
    val cashFlowType: CashFlowType? = null,
    val contactId: Long,
    val contactName: String,
    val amount: Long,
    val paidAmount: Long = 0L,
    val remainingDebt: Long = 0L,
    val transactionDate: Long,
    val shortDescription: String,
    val paymentMethodLabel: String? = null,
    val createdAt: Long = 0L
)

@Immutable
data class DashboardFinancialSummary(
    val totalCashIn: Long = 0L,
    val totalCashOut: Long = 0L,
    val totalPurchaseAmount: Long = 0L,
    val totalSaleAmount: Long = 0L,
    val totalReceivableDebt: Long = 0L, // Tổng tiền người khác còn nợ mình
    val totalPayableDebt: Long = 0L,    // Tổng tiền mình còn nợ người khác
    val receivableDebtorCount: Int = 0,
    val payableCreditorCount: Int = 0,
    val standaloneCashIn: Long = 0L,    // Thu độc lập ngoài đơn mua/bán
    val standaloneCashOut: Long = 0L    // Chi độc lập ngoài đơn mua/bán
)

@Immutable
data class PeriodBusinessStats(
    val periodLabel: String = "",
    val totalPurchaseAmount: Long = 0L,
    val totalSaleAmount: Long = 0L,
    val totalCashIn: Long = 0L,
    val totalCashOut: Long = 0L,
    val purchaseCattleCount: Int = 0,
    val saleCattleCount: Int = 0
)

@Immutable
data class PersonDebtSummaryItem(
    val contactId: Long,
    val fullName: String,
    val phone: String,
    val remainingDebt: Long,
    val relatedOrderCount: Int,
    val lastTransactionDate: Long
)

@Immutable
data class DebtHistoryEvent(
    val dateMillis: Long,
    val title: String,
    val description: String,
    val amount: Long,
    val isIncreaseDebt: Boolean,
    val isReceivableSide: Boolean // true = phía khách nợ mình, false = phía mình nợ người bán
)

@Immutable
data class PersonBidirectionalDebtDetail(
    val contact: Contact,
    // Phần 1: Người đó còn nợ mình (Phải thu - từ Bán hàng)
    val initialReceivableTotal: Long = 0L,
    val paidReceivableTotal: Long = 0L,
    val currentReceivableDebt: Long = 0L,
    val saleOrders: List<TradeOrderWithPayment> = emptyList(),
    val linkedCashInTransactions: List<CashTransactionWithContact> = emptyList(),
    // Phần 2: Mình còn nợ người đó (Phải trả - từ Mua hàng)
    val initialPayableTotal: Long = 0L,
    val paidPayableTotal: Long = 0L,
    val currentPayableDebt: Long = 0L,
    val purchaseOrders: List<TradeOrderWithPayment> = emptyList(),
    val linkedCashOutTransactions: List<CashTransactionWithContact> = emptyList(),
    // Các giao dịch Nhận/Chuyển độc lập (không gắn đơn)
    val standaloneTransactions: List<CashTransactionWithContact> = emptyList(),
    // Lịch sử biến động công nợ theo trình tự thời gian
    val debtTimeline: List<DebtHistoryEvent> = emptyList()
)
