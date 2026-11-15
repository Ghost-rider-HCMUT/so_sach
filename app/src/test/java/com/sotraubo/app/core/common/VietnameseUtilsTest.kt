package com.sotraubo.app.core.common

import com.sotraubo.app.core.common.format.CurrencyFormatter
import com.sotraubo.app.core.common.format.DateFormatter
import com.sotraubo.app.core.common.format.ReportPeriodType
import com.sotraubo.app.core.common.search.VietnameseTextNormalizer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VietnameseUtilsTest {

    @Test
    fun `normalizeName removes Vietnamese diacritics and handles d-bar`() {
        assertEquals("nguyen van a", VietnameseTextNormalizer.normalizeName("Nguyễn Văn A"))
        assertEquals("tran van dat", VietnameseTextNormalizer.normalizeName("  Trần   Văn Đạt "))
        assertEquals("le thi anh", VietnameseTextNormalizer.normalizeName("LÊ THỊ ÁNH"))
        assertEquals("hoang đuc vuong".replace('đ', 'd'), VietnameseTextNormalizer.normalizeName("Hoàng Đức Vượng"))
    }

    @Test
    fun `matchesQuery supports accented, unaccented and phone search`() {
        val name = "Nguyễn Văn Đạt"
        val phone = "0912 345 678"

        assertTrue(VietnameseTextNormalizer.matchesQuery(name, phone, "nguyen van a".take(10)))
        assertTrue(VietnameseTextNormalizer.matchesQuery(name, phone, "van dat"))
        assertTrue(VietnameseTextNormalizer.matchesQuery(name, phone, "NGUYỄN VĂN ĐẠT"))
        assertTrue(VietnameseTextNormalizer.matchesQuery(name, phone, "0912345"))
        assertTrue(VietnameseTextNormalizer.matchesQuery(name, phone, "345 678"))
        assertFalse(VietnameseTextNormalizer.matchesQuery(name, phone, "pham van b"))
        assertTrue(VietnameseTextNormalizer.matchesQuery(name, phone, "dat nguyen"))
        assertTrue(VietnameseTextNormalizer.matchesQuery(name, phone, "Nguyen Dat"))
        assertFalse(VietnameseTextNormalizer.matchesQuery(name, phone, "dat 9"))
    }

    @Test
    fun `CurrencyFormatter formats Long VND and reads Vietnamese words accurately`() {
        assertEquals("0 đ", CurrencyFormatter.formatVnd(0L))
        assertEquals("30.000.000 đ", CurrencyFormatter.formatVnd(30_000_000L))
        assertEquals("+10.000.000 đ", CurrencyFormatter.formatSignedVnd(10_000_000L, isPositive = true))
        assertEquals("-5.000.000 đ", CurrencyFormatter.formatSignedVnd(5_000_000L, isPositive = false))

        assertEquals(30_000_000L, CurrencyFormatter.parseDigitsToLong("30.000.000 đ"))
        assertEquals("Ba mươi triệu đồng chẵn", CurrencyFormatter.toVietnameseWords(30_000_000L))
        assertEquals("Mười lăm triệu năm trăm nghìn đồng chẵn", CurrencyFormatter.toVietnameseWords(15_500_000L))
        assertEquals("Hai mươiเอ็ด".take(0) + "Hai mươi mốt triệu đồng chẵn", CurrencyFormatter.toVietnameseWords(21_000_000L))
    }

    @Test
    fun `DateFormatter formats dd_MM_yyyy and computes period range`() {
        val sampleMillis = DateFormatter.parseDdMmYyyyToMillis("14/11/2026")!!
        assertEquals("14/11/2026", DateFormatter.formatDate(sampleMillis))

        val monthRange = DateFormatter.getPeriodRange(ReportPeriodType.THIS_MONTH, sampleMillis)
        assertTrue(monthRange.startMillis <= sampleMillis)
        assertTrue(monthRange.endMillis >= sampleMillis)
        assertEquals("Tháng 11/2026", monthRange.label)
    }
}
