package me.skillspro.auth.tokens

import me.skillspro.auth.models.Email
import me.skillspro.auth.tokens.models.Token
import me.skillspro.auth.tokens.models.TokenRequest
import me.skillspro.auth.tokens.models.TokenType
import me.skillspro.auth.tokens.redis.RedisTokenRepo
import me.skillspro.auth.tokens.redis.TokenHash
import me.skillspro.core.config.ConfigProperties
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class RedisTokenService(private val tokenRepo: RedisTokenRepo,
                        private val passwordEncoder: PasswordEncoder,
                        private val configProperties: ConfigProperties) : TokenService {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun createToken(tokenRequest: TokenRequest): Token {
        this.deleteExistingToken(tokenRequest.userId, tokenRequest.type)
        return this.saveNewToken(tokenRequest)
    }

    override fun deleteExistingToken(userId: String, type: TokenType) {
        val foundToken = this.tokenRepo.findByIdOrNull(TokenHash.createId(userId, type.name))
        if (foundToken != null) {
            logger.debug("found token of ${foundToken.id} for $userId")
            this.tokenRepo.delete(foundToken)
        }
    }


    private fun saveNewToken(tokenRequest: TokenRequest): Token {
        val token = tokenRequest.generateToken()
        this.tokenRepo.save(TokenHash(passwordEncoder.encode(token.value), tokenRequest.userId,
                tokenRequest.type.name, configProperties.redisTokenTtlSecs))
        return token
    }

    override fun isTokenValid(email: Email, token: Token, type: TokenType): Boolean {
        val firstToken = this.tokenRepo.findByIdOrNull(TokenHash.createId(email.value, type.name))
        logger.debug("first token is : {}", firstToken)
        if (firstToken != null) {
            return this.passwordEncoder.matches(token.value, firstToken.token)
        }
        return false
    }

    fun getUserTokens(user: String, type: String) = this.tokenRepo
            .findById(TokenHash.createId(user, type))
            .map { mapOf("user" to it.user, "type" to it.type, "ttl" to it.ttl) }.get()
}