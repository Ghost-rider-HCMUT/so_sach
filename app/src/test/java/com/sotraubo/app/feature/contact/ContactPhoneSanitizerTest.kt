package com.sotraubo.app.feature.contact

import org.junit.Assert.assertEquals
import org.junit.Test

class ContactPhoneSanitizerTest {

    @Test
    fun `sanitizePhoneInput strips letters and special characters while keeping valid phone`() {
        assertEquals("0912 345 678", sanitizePhoneInput("0912 345 678"))
        assertEquals("+84912345678", sanitizePhoneInput("+84912345678"))
        assertEquals("0912345678", sanitizePhoneInput("abc0912-345.678!@#"))
        assertEquals("0912 345", sanitizePhoneInput("  0912   345"))
        assertEquals("0123456789012345", sanitizePhoneInput("01234567890123456789"))
    }
}
