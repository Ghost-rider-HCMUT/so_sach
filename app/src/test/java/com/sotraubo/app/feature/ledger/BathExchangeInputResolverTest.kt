package com.sotraubo.app.feature.ledger

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BathExchangeInputResolverTest {

    // --- Chiều Chuyển (VND_TO_THB) ---

    @Test
    fun `transfer with bath and rate only auto-multiplies to vnd`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "VND_TO_THB",
            bahtAmount = 1_000L,
            rate = 750L,
            vndAmount = 0L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Valid)
        val valid = result as BathExchangeResolution.Valid
        assertEquals(750_000L, valid.resolvedVndAmount)
        assertEquals(750_000L, valid.resolvedTheoreticalVndAmount)
    }

    @Test
    fun `transfer with vnd only succeeds without bath or rate`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "VND_TO_THB",
            bahtAmount = 0L,
            rate = 0L,
            vndAmount = 5_000_000L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Valid)
        val valid = result as BathExchangeResolution.Valid
        assertEquals(5_000_000L, valid.resolvedVndAmount)
    }

    @Test
    fun `transfer with bath and vnd succeeds without rate`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "VND_TO_THB",
            bahtAmount = 10_000L,
            rate = 0L,
            vndAmount = 7_350_000L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Valid)
        val valid = result as BathExchangeResolution.Valid
        assertEquals(7_350_000L, valid.resolvedVndAmount)
    }

    @Test
    fun `transfer with bath and rate and explicit vnd prefers user vnd`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "VND_TO_THB",
            bahtAmount = 1_000L,
            rate = 750L,
            vndAmount = 760_000L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Valid)
        assertEquals(760_000L, (result as BathExchangeResolution.Valid).resolvedVndAmount)
    }

    @Test
    fun `transfer with bath only fails and asks for rate or vnd`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "VND_TO_THB",
            bahtAmount = 1_000L,
            rate = 0L,
            vndAmount = 0L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Invalid)
        assertTrue((result as BathExchangeResolution.Invalid).errorMessage.contains("Tỉ giá"))
    }

    @Test
    fun `transfer with rate only fails and asks for bath or vnd`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "VND_TO_THB",
            bahtAmount = 0L,
            rate = 750L,
            vndAmount = 0L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Invalid)
        assertTrue((result as BathExchangeResolution.Invalid).errorMessage.contains("Số Bath"))
    }

    @Test
    fun `transfer with all zero fails`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "VND_TO_THB",
            bahtAmount = 0L,
            rate = 0L,
            vndAmount = 0L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Invalid)
    }

    // --- Chiều Nhận (THB_TO_VND) ---

    @Test
    fun `receive with bath and rate only auto-multiplies to theoretical vnd`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "THB_TO_VND",
            bahtAmount = 2_000L,
            rate = 740L,
            vndAmount = 0L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Valid)
        val valid = result as BathExchangeResolution.Valid
        assertEquals(0L, valid.resolvedVndAmount)
        assertEquals(1_480_000L, valid.resolvedTheoreticalVndAmount)
    }

    @Test
    fun `receive with actual vnd only succeeds without bath or rate`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "THB_TO_VND",
            bahtAmount = 0L,
            rate = 0L,
            vndAmount = 40_000_000L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Valid)
        val valid = result as BathExchangeResolution.Valid
        assertEquals(40_000_000L, valid.resolvedVndAmount)
        assertEquals(40_000_000L, valid.resolvedTheoreticalVndAmount)
    }

    @Test
    fun `receive with bath and partial vnd succeeds without rate`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "THB_TO_VND",
            bahtAmount = 85_000L,
            rate = 0L,
            vndAmount = 40_000_000L,
            theoreticalVndAmount = 65_000_000L
        )
        assertTrue(result is BathExchangeResolution.Valid)
        val valid = result as BathExchangeResolution.Valid
        assertEquals(40_000_000L, valid.resolvedVndAmount)
        assertEquals(65_000_000L, valid.resolvedTheoreticalVndAmount)
    }

    @Test
    fun `receive with bath only and no vnd fails`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "THB_TO_VND",
            bahtAmount = 1_000L,
            rate = 0L,
            vndAmount = 0L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Invalid)
        assertTrue((result as BathExchangeResolution.Invalid).errorMessage.contains("Giá bán"))
    }

    @Test
    fun `receive with bath costRate and sellRate computes both cost and receivable`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "THB_TO_VND",
            bahtAmount = 2_000_000L,
            rate = 1_050L,
            vndAmount = 0L,
            theoreticalVndAmount = 0L,
            costRate = 1_000L,
            costVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Valid)
        val valid = result as BathExchangeResolution.Valid
        assertEquals(2_000_000_000L, valid.resolvedCostVndAmount)
        assertEquals(2_100_000_000L, valid.resolvedTheoreticalVndAmount)
        assertEquals(0L, valid.resolvedVndAmount)
    }

    @Test
    fun `receive with all zero fails`() {
        val result = BathExchangeInputResolver.resolve(
            directionCode = "THB_TO_VND",
            bahtAmount = 0L,
            rate = 0L,
            vndAmount = 0L,
            theoreticalVndAmount = 0L
        )
        assertTrue(result is BathExchangeResolution.Invalid)
    }
}
