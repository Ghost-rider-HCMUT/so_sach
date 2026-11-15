package com.sotraubo.app.core.common.format

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CurrencyExpressionEvaluatorTest {

    @Test
    fun `evaluate single formatted number`() {
        assertEquals(15_000_000L, CurrencyExpressionEvaluator.evaluate("15.000.000"))
    }

    @Test
    fun `evaluate multiplication for multiple cattle`() {
        assertEquals(45_000_000L, CurrencyExpressionEvaluator.evaluate("15000000×3"))
    }

    @Test
    fun `evaluate respects operator precedence`() {
        assertEquals(47_000_000L, CurrencyExpressionEvaluator.evaluate("2000000+15000000×3"))
    }

    @Test
    fun `evaluate division and subtraction`() {
        assertEquals(14_500_000L, CurrencyExpressionEvaluator.evaluate("60000000÷4-500000"))
    }

    @Test
    fun `evaluate division by zero returns null`() {
        assertNull(CurrencyExpressionEvaluator.evaluate("10000000÷0"))
    }

    @Test
    fun `evaluate ignores trailing operator for live preview`() {
        assertEquals(15_000_000L, CurrencyExpressionEvaluator.evaluate("15000000×"))
    }

    @Test
    fun `evaluate clamps negative result to zero`() {
        assertEquals(0L, CurrencyExpressionEvaluator.evaluate("1000000-3000000"))
    }

    @Test
    fun `formatExpressionForDisplay formats thousands and spaces operators`() {
        assertEquals(
            "15.000.000 × 3 + 500.000",
            CurrencyExpressionEvaluator.formatExpressionForDisplay("15000000×3+500000")
        )
    }
}
