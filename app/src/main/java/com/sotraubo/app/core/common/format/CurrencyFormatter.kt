package com.sotraubo.app.core.common.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

/**
 * Bộ định dạng tiền tệ VNĐ (sử dụng kiểu Long tuyệt đối không sai số)
 * và đọc số tiền thành chữ tiếng Việt tự nhiên cho hộ kinh doanh.
 */
object CurrencyFormatter {

    private val vietnameseSymbols = DecimalFormatSymbols(Locale("vi", "VN")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }

    private val numberFormat: ThreadLocal<DecimalFormat> = ThreadLocal.withInitial {
        DecimalFormat("#,###", vietnameseSymbols)
    }

    /**
     * Định dạng số nguyên Long sang chuỗi tiền tệ có hậu tố " đ".
     * Ví dụ: 30000000L -> "30.000.000 đ"
     */
    fun formatVnd(amount: Long): String {
        val formatted = numberFormat.get()?.format(abs(amount)) ?: abs(amount).toString()
        return if (amount < 0) "-$formatted đ" else "$formatted đ"
    }

    /**
     * Định dạng có dấu cộng/trừ rõ ràng cho dòng tiền Nhận/Chuyển.
     */
    fun formatSignedVnd(amount: Long, isPositive: Boolean): String {
        val formatted = numberFormat.get()?.format(abs(amount)) ?: abs(amount).toString()
        val prefix = if (isPositive) "+" else "-"
        return "$prefix$formatted đ"
    }

    /**
     * Định dạng chỉ lấy phần số có dấu chấm phân cách (dùng trong ô nhập liệu).
     * Ví dụ: 30000000L -> "30.000.000"
     */
    fun formatNumberOnly(amount: Long): String {
        if (amount == 0L) return "0"
        return numberFormat.get()?.format(amount) ?: amount.toString()
    }

    /**
     * Chuyển chuỗi người dùng nhập (có thể chứa dấu chấm, khoảng trắng) về số nguyên Long.
     */
    fun parseDigitsToLong(rawInput: String): Long {
        val digitsOnly = rawInput.filter { it.isDigit() }
        if (digitsOnly.isEmpty()) return 0L
        return digitsOnly.take(15).toLongOrNull() ?: 0L
    }

    /**
     * Định dạng rút gọn trên trục biểu đồ (Ví dụ: 30.000.000 -> "30 tr", 1.500.000 -> "1,5 tr").
     */
    fun formatCompactVnd(amount: Long): String {
        val absAmount = abs(amount)
        return when {
            absAmount >= 1_000_000_000L -> {
                val billions = absAmount / 1_000_000_000.0
                val text = if (absAmount % 1_000_000_000L == 0L) {
                    (absAmount / 1_000_000_000L).toString()
                } else {
                    String.format(Locale("vi", "VN"), "%.1f", billions)
                }
                "$text tỷ"
            }
            absAmount >= 1_000_000L -> {
                val millions = absAmount / 1_000_000.0
                val text = if (absAmount % 1_000_000L == 0L) {
                    (absAmount / 1_000_000L).toString()
                } else {
                    String.format(Locale("vi", "VN"), "%.1f", millions)
                }
                "$text tr"
            }
            absAmount >= 1_000L -> "${absAmount / 1_000L} nghìn"
            else -> "$absAmount đ"
        }
    }

    private val digitWords = arrayOf(
        "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"
    )

    /**
     * Đọc số tiền thành chữ tiếng Việt để chủ hộ kiểm tra nhanh tránh nhập thừa/thiếu số 0.
     * Ví dụ: 30000000L -> "Ba mươi triệu đồng chẵn"
     */
    fun toVietnameseWords(amount: Long): String {
        if (amount == 0L) return "Không đồng"
        if (amount < 0L) return "Âm " + toVietnameseWords(abs(amount)).replaceFirstChar { it.lowercase() }

        val units = arrayOf("", "nghìn", "triệu", "tỷ", "nghìn tỷ")
        var remaining = amount
        val groups = mutableListOf<Int>()
        while (remaining > 0) {
            groups.add((remaining % 1000).toInt())
            remaining /= 1000
        }

        val parts = mutableListOf<String>()
        for (i in groups.indices.reversed()) {
            val groupValue = groups[i]
            if (groupValue > 0) {
                val isHighestGroup = (i == groups.lastIndex)
                val groupText = readThreeDigits(groupValue, !isHighestGroup)
                val unitName = units.getOrElse(i) { "" }
                parts.add(if (unitName.isNotEmpty()) "$groupText $unitName" else groupText)
            }
        }

        val sentence = parts.joinToString(" ").trim()
        val capitalized = sentence.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale("vi", "VN")) else it.toString()
        }
        val isRoundThousand = (amount % 1000L == 0L)
        return if (isRoundThousand) "$capitalized đồng chẵn" else "$capitalized đồng"
    }

    private fun readThreeDigits(number: Int, readZeroHundred: Boolean): String {
        val hundreds = number / 100
        val tens = (number % 100) / 10
        val ones = number % 10
        val builder = mutableListOf<String>()

        if (hundreds > 0 || readZeroHundred) {
            builder.add("${digitWords[hundreds]} trăm")
        }

        when (tens) {
            0 -> {
                if (ones > 0 && builder.isNotEmpty()) {
                    builder.add("lẻ")
                }
            }
            1 -> builder.add("mười")
            else -> builder.add("${digitWords[tens]} mươi")
        }

        if (ones > 0) {
            val oneWord = when {
                ones == 1 && tens > 1 -> "mốt"
                ones == 4 && tens > 1 -> "tư"
                ones == 5 && tens >= 1 -> "lăm"
                else -> digitWords[ones]
            }
            builder.add(oneWord)
        }

        return builder.joinToString(" ")
    }
}
