package me.skillspro.auth.verification

import me.skillspro.auth.UserService
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.User
import me.skillspro.auth.tokens.TokenService
import me.skillspro.auth.tokens.models.Token
import me.skillspro.auth.tokens.models.TokenRequest
import me.skillspro.auth.tokens.models.TokenType
import me.skillspro.core.config.ConfigProperties
import me.skillspro.core.data.NotificationEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class AccountVerificationService(private val tokenService: TokenService,
                                 private val events: ApplicationEventPublisher,
                                 private val userService: UserService,
                                 private val configProperties: ConfigProperties) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Async
    @EventListener
    fun userCreated(user: User) {
        logger.info("Event :: user created :: verification :: ${user.email.value}")
        this.createTokenAndSend(user)
    }

    fun createTokenAndSend(user: User) {
        val token = this.tokenService.createToken(TokenRequest(user.email, TokenType.ACCOUNT_VERIFICATION))
        this.events.publishEvent(
                NotificationEvent(user.email.value, "mail.verification", configProperties
                        .emailVerificationSubject,
                        mapOf("token" to token.value))
        )
    }

    fun verifyEmail(email: Email, token: Token) {
        val tokenValid = this.tokenService.isTokenValid(email, token, TokenType.ACCOUNT_VERIFICATION)
        if (!tokenValid) {
            throw IllegalArgumentException("Token is invalid: " + email.value)
        }
        this.userService.validateAccount(email)
    }

    fun resendVerification(email: Email) {
        logger.debug("resending verification token [{}]", email.value)
        userService.findAccount(email).also(this::createTokenAndSend)
    }
}