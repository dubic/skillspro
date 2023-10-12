package me.skillspro.auth.models

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class EmailTest {

    @Test
    fun `should be defined, trimmed and lowercase`() {
        assertDoesNotThrow { Email("udubic@gmail.com", null) }
        assertEquals("udubic@gmail.com", Email(" UDUBIC@GMAIL.com ", null).value)
    }

    @Test
    fun `should be invalid`() {
        assertThrows<IllegalArgumentException> { Email("udubic", null) }
        assertThrows<IllegalArgumentException> { Email(" ", null) }
    }

    @Test
    fun `Should be unverified`() {
        assertEquals(Email("udubic@gmail.com", null).isVerified(), false)
    }

    @Test
    fun `Should be verified`() {
        assertEquals(Email("udubic@gmail.com", true).isVerified(), true)
    }
}