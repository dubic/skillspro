package me.skillspro.auth.models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NameTest {
    @Test
    fun `should be between 3 and 70 characters`() {
        assertThrows<IllegalArgumentException> { Name("") }
        assertThrows<IllegalArgumentException> {
            Name("I transitioned into tech after several years of exploring different careers")
        }
    }

    @Test
    fun `should be alphabets only`() {
        assertThrows<IllegalArgumentException> { Name("...") }
        assertThrows<IllegalArgumentException> { Name("abu{0") }

    }

    @Test
    fun `should be defined`() {
        assertDoesNotThrow { Name("Abu") }
        assertDoesNotThrow { Name("Dubic uzuegbu") }
    }
}
