package com.sotraubo.app.core.common.format

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Bộ phân tích và tính toán biểu thức số học (+, -, ×, ÷) cho bàn phím nhập tiền tùy chỉnh.
 * Hỗ trợ thứ tự ưu tiên nhân/chia trước cộng/trừ, số lớn (VND) không tràn Long trung gian,
 * và định dạng dấu chấm phân cách hàng nghìn ngay trong biểu thức.
 */
object CurrencyExpressionEvaluator {

    private val operators = setOf('+', '-', '×', '÷', '*', '/')

    fun isOperator(ch: Char): Boolean = ch in operators

    fun normalizeOperator(ch: Char): Char = when (ch) {
        '*' -> '×'
        '/' -> '÷'
        else -> ch
    }

    /**
     * Tính toán giá trị biểu thức. Trả về null nếu chia cho 0 hoặc biểu thức rỗng.
     * Nếu biểu thức kết thúc bằng phép tính dở dang (ví dụ "15000000×"), tự động bỏ phép tính cuối để xem trước.
     */
    fun evaluate(rawExpression: String): Long? {
        val tokens = tokenize(rawExpression)
        if (tokens.isEmpty()) return null

        // Bỏ toán tử treo ở cuối nếu người dùng đang gõ dở
        val cleanTokens = if (tokens.last() is Token.Op) tokens.dropLast(1) else tokens
        if (cleanTokens.isEmpty()) return null

        // Bước 1: Thực hiện × và ÷ từ trái sang phải
        val afterMulDiv = mutableListOf<Token>()
        var i = 0
        while (i < cleanTokens.size) {
            val token = cleanTokens[i]
            if (token is Token.Op && (token.symbol == '×' || token.symbol == '÷')) {
                val left = (afterMulDiv.removeAt(afterMulDiv.lastIndex) as Token.Num).value
                val right = (cleanTokens[i + 1] as Token.Num).value
                val computed = if (token.symbol == '×') {
                    left.multiply(right)
                } else {
                    if (right.compareTo(BigDecimal.ZERO) == 0) return null
                    left.divide(right, 0, RoundingMode.HALF_UP)
                }
                afterMulDiv.add(Token.Num(computed))
                i += 2
            } else {
                afterMulDiv.add(token)
                i += 1
            }
        }

        // Bước 2: Thực hiện + và - từ trái sang phải
        var result = (afterMulDiv.first() as Token.Num).value
        var j = 1
        while (j < afterMulDiv.size) {
            val op = (afterMulDiv[j] as Token.Op).symbol
            val right = (afterMulDiv[j + 1] as Token.Num).value
            result = if (op == '+') result.add(right) else result.subtract(right)
            j += 2
        }

        return result.toLong().coerceAtLeast(0L)
    }

    /**
     * Định dạng biểu thức thô (chỉ chứa chữ số và +-×÷) thành chuỗi dễ đọc có dấu chấm hàng nghìn:
     * Ví dụ: "15000000×3+500000" -> "15.000.000 × 3 + 500.000"
     */
    fun formatExpressionForDisplay(rawExpression: String): String {
        if (rawExpression.isBlank()) return ""
        val sb = StringBuilder()
        val currentDigits = StringBuilder()

        fun flushDigits() {
            if (currentDigits.isNotEmpty()) {
                val num = currentDigits.toString().toLongOrNull()
                if (num != null) {
                    sb.append(CurrencyFormatter.formatNumberOnly(num))
                } else {
                    sb.append(currentDigits)
                }
                currentDigits.clear()
            }
        }

        for (ch in rawExpression) {
            when {
                ch.isDigit() -> currentDigits.append(ch)
                isOperator(ch) -> {
                    flushDigits()
                    sb.append(' ').append(normalizeOperator(ch)).append(' ')
                }
            }
        }
        flushDigits()
        return sb.toString().trimEnd()
    }

    private sealed interface Token {
        data class Num(val value: BigDecimal) : Token
        data class Op(val symbol: Char) : Token
    }

    private fun tokenize(raw: String): List<Token> {
        val tokens = mutableListOf<Token>()
        val numBuf = StringBuilder()

        fun flushNum() {
            if (numBuf.isNotEmpty()) {
                val parsed = numBuf.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO
                tokens.add(Token.Num(parsed))
                numBuf.clear()
            }
        }

        for (ch in raw) {
            when {
                ch.isDigit() -> numBuf.append(ch)
                isOperator(ch) -> {
                    flushNum()
                    if (tokens.isNotEmpty() && tokens.last() is Token.Num) {
                        tokens.add(Token.Op(normalizeOperator(ch)))
                    }
                }
            }
        }
        flushNum()
        return tokens
    }
}
