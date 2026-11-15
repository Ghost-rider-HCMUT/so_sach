package com.sotraubo.app.core.common.format

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class InstallmentNoteFormatterTest {

    private fun dateMillis(year: Int, month: Int, day: Int): Long {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))
        cal.set(year, month - 1, day, 12, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    @Test
    fun appendInstallmentNote_withEmptyExistingNoteAndRemainingDebt() {
        val result = InstallmentNoteFormatter.appendInstallmentNote(
            existingNote = "",
            dateMillis = dateMillis(2026, 11, 15),
            addedAmount = 15_000_000L,
            newTotalPaid = 55_000_000L,
            remainingAfter = 10_000_000L,
            actionLabel = "Khách chuyển thêm",
            installmentNote = "CK ACB"
        )
        assertEquals(
            "• 15/11/2026: Khách chuyển thêm 15.000.000 đ (Tổng đã trả: 55.000.000 đ, còn thiếu: 10.000.000 đ) - CK ACB",
            result
        )
    }

    @Test
    fun appendInstallmentNote_withExistingNoteAndFullyPaid() {
        val result = InstallmentNoteFormatter.appendInstallmentNote(
            existingNote = "Chia nhiều đợt",
            dateMillis = dateMillis(2026, 11, 16),
            addedAmount = 10_000_000L,
            newTotalPaid = 65_000_000L,
            remainingAfter = 0L,
            actionLabel = "Khách chuyển thêm",
            installmentNote = ""
        )
        assertEquals(
            "Chia nhiều đợt\n• 16/11/2026: Khách chuyển thêm 10.000.000 đ (Tổng: 65.000.000 đ - Đã đủ)",
            result
        )
    }
}
