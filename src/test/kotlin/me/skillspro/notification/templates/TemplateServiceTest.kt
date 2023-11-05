package me.skillspro.notification.templates

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class TemplateServiceTest {

    @Test
    fun resolveToString() {
        val actual = """
            <html>
                            <h4>Welcome to Skillspro</h4>
                             <p>To verify your account, use the token: <b>123456</b></p>
                        </html>
        """.trimIndent()
        val resolved = TemplateService()
                .resolveToString("mail.verification", mapOf("token" to "123456"))
        assertEquals(actual, resolved)
    }
}