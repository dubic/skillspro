package me.skillspro.auth.models

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException

class PasswordTest {

    @Test
    fun `should be between 8 and 100 characters`() {
        assertThrows<IllegalArgumentException> { Password("") }
        assertThrows<IllegalArgumentException> {
            Password(
                    "I transitioned into tech after several years of exploring different careers.I love making things. I've written and produced electronic music, built a podcast, wrote essays on creative work and tried various business ideas.passionate about the future of work, tech and how to make things better.",
            )
        }
    }

    @Test
    fun `should be defined`() {
        assertDoesNotThrow{ Password("dinchah7199") }
        assertEquals("dinchah7199", Password("dinchah7199").plain)
    }
}