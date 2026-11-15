package com.sotraubo.app.core.common.search

import java.text.Normalizer
import java.util.Locale

/**
 * Bộ chuẩn hóa chuỗi tiếng Việt phục vụ tìm kiếm không dấu và chuẩn hóa số điện thoại.
 * Ví dụ: "Nguyễn Văn Đạt" -> "nguyen van dat"
 */
object VietnameseTextNormalizer {

    private val DIACRITICS_REGEX = Regex("\\p{InCombiningDiacriticalMarks}+")
    private val MULTIPLE_SPACES_REGEX = Regex("\\s+")
    private val NON_DIGIT_REGEX = Regex("[^0-9]")

    fun normalizeName(input: String): String {
        if (input.isBlank()) return ""
        val trimmed = input.trim().lowercase(Locale("vi", "VN"))
        val decomposed = Normalizer.normalize(trimmed, Normalizer.Form.NFD)
        val withoutMarks = DIACRITICS_REGEX.replace(decomposed, "")
            .replace('đ', 'd')
            .replace('Đ', 'd')
        return MULTIPLE_SPACES_REGEX.replace(withoutMarks, " ").trim()
    }

    fun normalizePhone(phone: String?): String {
        if (phone.isNullOrBlank()) return ""
        return NON_DIGIT_REGEX.replace(phone, "")
    }

    private val PHONE_QUERY_REGEX = Regex("^[0-9\\s+.\\-()]+$")

    /**
     * Khớp theo TỪNG TỪ (không phụ thuộc thứ tự, không dấu, không phân biệt hoa thường):
     * "dat nguyen" vẫn tìm thấy "Nguyễn Văn Đạt".
     * Chỉ so với số điện thoại khi ô tìm kiếm thuần là số (tránh khớp nhầm "Hai 2").
     */
    fun matchesQuery(fullName: String, phone: String?, rawQuery: String): Boolean {
        val trimmedQuery = rawQuery.trim()
        if (trimmedQuery.isEmpty()) return true

        val normalizedName = normalizeName(fullName)
        val tokens = normalizeName(trimmedQuery).split(' ').filter { it.isNotEmpty() }
        if (tokens.isNotEmpty() && tokens.all { normalizedName.contains(it) }) return true

        if (PHONE_QUERY_REGEX.matches(trimmedQuery)) {
            val phoneDigitsQuery = normalizePhone(trimmedQuery)
            if (phoneDigitsQuery.isNotEmpty() && normalizePhone(phone).contains(phoneDigitsQuery)) return true
        }
        return false
    }
}