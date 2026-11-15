package com.sotraubo.app.core.common.format

/**
 * Formats and appends clear installment payment history lines to a record's note field.
 */
object InstallmentNoteFormatter {

    fun appendInstallmentNote(
        existingNote: String,
        dateMillis: Long,
        addedAmount: Long,
        newTotalPaid: Long,
        remainingAfter: Long,
        actionLabel: String,
        installmentNote: String = ""
    ): String {
        val dateStr = DateFormatter.formatDate(dateMillis)
        val addedStr = CurrencyFormatter.formatVnd(addedAmount)
        val totalStr = CurrencyFormatter.formatVnd(newTotalPaid)
        val statusStr = if (remainingAfter > 0L) {
            "Tổng đã trả: $totalStr, còn thiếu: ${CurrencyFormatter.formatVnd(remainingAfter)}"
        } else {
            "Tổng: $totalStr - Đã đủ"
        }
        val extra = installmentNote.trim()
        val line = buildString {
            append("• ")
            append(dateStr)
            append(": ")
            append(actionLabel)
            append(" ")
            append(addedStr)
            append(" (")
            append(statusStr)
            append(")")
            if (extra.isNotEmpty()) {
                append(" - ")
                append(extra)
            }
        }
        val base = existingNote.trim()
        return if (base.isEmpty()) line else "$base\n$line"
    }
}
