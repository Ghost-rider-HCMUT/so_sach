package com.sotraubo.app.core.data

import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val vietnameseMessage: String) : ValidationResult
}

/**
 * Bộ kiểm tra quy tắc nghiệp vụ tài chính & công nợ theo đúng 03_BUSINESS_RULES.md.
 * Toàn bộ thông báo lỗi hiển thị 100% bằng tiếng Việt.
 */
object FinancialRuleValidator {

    fun validateContactName(fullName: String): ValidationResult {
        if (fullName.trim().isEmpty()) {
            return ValidationResult.Invalid("Vui lòng nhập họ và tên người liên quan.")
        }
        return ValidationResult.Valid
    }

    fun validateTradeOrderInput(
        contactId: Long?,
        cattleQuantity: Int,
        description: String,
        totalAmount: Long,
        initialPaidAmount: Long,
        existingSubsequentPaidAmount: Long = 0L,
        isPurchase: Boolean
    ): ValidationResult {
        if (contactId == null || contactId <= 0L) {
            val role = if (isPurchase) "người bán" else "người mua"
            return ValidationResult.Invalid("Vui lòng chọn $role trước khi lưu đơn.")
        }
        if (cattleQuantity <= 0) {
            return ValidationResult.Invalid("Số lượng trâu/bò phải lớn hơn 0.")
        }
        if (description.trim().isEmpty()) {
            return ValidationResult.Invalid("Vui lòng nhập mô tả trâu/bò hoặc lô hàng.")
        }
        if (totalAmount <= 0L) {
            val label = if (isPurchase) "Tổng tiền mua" else "Tổng tiền bán"
            return ValidationResult.Invalid("$label phải lớn hơn 0 đ.")
        }
        if (initialPaidAmount < 0L) {
            return ValidationResult.Invalid("Số tiền thanh toán không được nhỏ hơn 0 đ.")
        }
        val totalPaidAfterSave = initialPaidAmount + existingSubsequentPaidAmount
        if (totalPaidAfterSave > totalAmount) {
            return if (existingSubsequentPaidAmount > 0L) {
                ValidationResult.Invalid(
                    "Tổng số tiền đã thanh toán (${CurrencyFormatter.formatVnd(totalPaidAfterSave)}) " +
                        "không được vượt quá tổng tiền đơn hàng (${CurrencyFormatter.formatVnd(totalAmount)})."
                )
            } else {
                val paidLabel = if (isPurchase) "Số tiền đã trả" else "Số tiền đã nhận"
                val totalLabel = if (isPurchase) "tổng tiền mua" else "tổng tiền bán"
                ValidationResult.Invalid(
                    "$paidLabel không được vượt quá $totalLabel (${CurrencyFormatter.formatVnd(totalAmount)})."
                )
            }
        }
        return ValidationResult.Valid
    }

    fun validateCashTransactionInput(
        contactId: Long?,
        amount: Long,
        linkedOrderTotalAmount: Long?,
        linkedOrderAlreadyPaidExcludingCurrent: Long?,
        linkedOrderDateMillis: Long?,
        isCashIn: Boolean
    ): ValidationResult {
        if (contactId == null || contactId <= 0L) {
            val role = if (isCashIn) "người đưa tiền" else "người nhận tiền"
            return ValidationResult.Invalid("Vui lòng chọn $role trước khi lưu giao dịch.")
        }
        if (amount <= 0L) {
            val label = if (isCashIn) "Số tiền nhận" else "Số tiền chuyển"
            return ValidationResult.Invalid("$label phải lớn hơn 0 đ.")
        }
        if (linkedOrderTotalAmount != null && linkedOrderAlreadyPaidExcludingCurrent != null) {
            val remainingAllowed = (linkedOrderTotalAmount - linkedOrderAlreadyPaidExcludingCurrent).coerceAtLeast(0L)
            if (amount > remainingAllowed) {
                val orderKind = if (isCashIn) "Đơn bán" else "Đơn mua"
                val dateText = linkedOrderDateMillis?.let { " ngày ${DateFormatter.formatDate(it)}" } ?: ""
                return ValidationResult.Invalid(
                    "$orderKind$dateText chỉ còn nợ ${CurrencyFormatter.formatVnd(remainingAllowed)}. " +
                        "Không thể nhập số tiền trả nợ lớn hơn số tiền còn nợ."
                )
            }
        }
        return ValidationResult.Valid
    }
}
