package me.skillspro.auth.tokens.models

import me.skillspro.auth.models.Email
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class TokenRequestTest {


    @Test
    fun `new account verification token request`() {
        val email = Email("udubic@gmail.com", null)
        val tokenRequest = TokenRequest(email, TokenType.ACCOUNT_VERIFICATION)
        val token = tokenRequest.generateToken()
        assertEquals(token.value.length, 6)
    }
}