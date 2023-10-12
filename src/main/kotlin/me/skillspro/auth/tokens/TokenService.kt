package me.skillspro.auth.tokens

import me.skillspro.auth.models.Email
import me.skillspro.auth.tokens.models.Token
import me.skillspro.auth.tokens.models.TokenRequest
import me.skillspro.auth.tokens.models.TokenType

interface TokenService {
     fun createToken(tokenRequest: TokenRequest): Token
     fun isTokenValid(email: Email, token: Token, type: TokenType): Boolean
}