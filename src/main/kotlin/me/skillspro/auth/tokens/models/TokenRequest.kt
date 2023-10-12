package me.skillspro.auth.tokens.models

import me.skillspro.auth.models.Email
import kotlin.math.pow
import kotlin.random.Random

class TokenRequest(email: Email, val type: TokenType) {
    val userId: String = email.value

    private fun calculateDigits(type: TokenType): Int {
        return when (type) {
            TokenType.ACCOUNT_VERIFICATION -> 6
            TokenType.PASSWORD -> 8
        }
    }

    private fun generateRandomDigits(): String {
        val digits = calculateDigits(this.type)
        val rand = Random(System.currentTimeMillis()).nextDouble(10.0.pow(digits) - 1)
        return String.format("%0${digits}d", rand.toInt())
    }

    fun generateToken(): Token = Token(this.generateRandomDigits())
}