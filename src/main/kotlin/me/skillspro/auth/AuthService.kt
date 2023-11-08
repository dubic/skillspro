package me.skillspro.auth

import me.skillspro.auth.dao.UserRepo
import me.skillspro.auth.dto.Account
import me.skillspro.auth.dto.AuthResponse
import me.skillspro.auth.dto.SocialLoginRequest
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.auth.session.SessionService
import me.skillspro.auth.tokens.TokenService
import me.skillspro.auth.tokens.models.TokenRequest
import me.skillspro.auth.tokens.models.TokenType
import me.skillspro.core.config.ConfigProperties
import me.skillspro.core.data.NotificationEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(private val passwordService: PasswordService,
                  private val sessionService: SessionService,
                  private val userRepo: UserRepo,
                  private val userService: UserService,
                  private val events: ApplicationEventPublisher,
                  private val configProperties: ConfigProperties,
                  private val tokenService: TokenService) {
    fun isAuthenticationValid(token: String): Boolean {
        return sessionService.userInSession(token) != null
    }

    fun login(email: Email, password: Password): AuthResponse {
        logger.info("Attempting authentication for : ${email.value}")
        val user = this.validateAccount(email, password)
        val authToken = this.createAuthenticationToken(user)
        this.sessionService.createSession(authToken, user)
        return AuthResponse(authToken, Account(user.name.value, user.email.value, user.isVerified()))
    }

    fun validateAccount(email: Email, password: Password): User {
        val dbUser = this.userRepo.findByIdOrNull(email.value)
        if (dbUser == null) {
            this.logger.warn("wrong credentials email" + email.value)
            throw BadCredentialsException("wrong credentials")
        }
        if (dbUser.password == null) {
            this.logger.warn("No password for account [{}]", email.value)
            throw BadCredentialsException("This account [${email.value}] has no password associated with" +
                    " it. Kindly login with Google")
        }

        if (!passwordService.compare(password, dbUser.password!!)) {
            this.logger.warn("wrong credentials password" + email.value)
            throw BadCredentialsException("wrong credentials")
        }
        return User.from(dbUser)
    }

    fun createAuthenticationToken(user: User): String {
        return UUID.randomUUID().toString()
    }

    fun logout() {
        TODO("Not yet implemented")
    }

    fun authenticate(email: Email): AuthResponse {
        val dbUser = userRepo.findByIdOrNull(email.value)
        val user = User.from(dbUser!!)
        return this.createAuthenticationSession(user)
    }

    private fun createAuthenticationSession(user: User): AuthResponse {
        val token = this.createAuthenticationToken(user)
        this.sessionService.createSession(token, user)
        return AuthResponse(token, Account(user.name.value, user.email.value, user.isVerified()))
    }

    fun loginSocial(socialLoginRequest: SocialLoginRequest): AuthResponse {
        val user = userService.getOrCreateUser(socialLoginRequest)
        return createAuthenticationSession(user)
    }

    fun forgotPassword(email: Email) {
        logger.debug("Starting password reset process :: {}", email.value)
        val user = userService.findAccount(email)
        val createdToken = tokenService.createToken(TokenRequest(email, TokenType.PASSWORD))
        this.events.publishEvent(
                NotificationEvent(user.email.value, "mail.password", configProperties
                        .passwordForgotSubject,
                        mapOf("token" to createdToken.value,
                                "name" to user.name.value))
        )
    }

    private val logger = LoggerFactory.getLogger(javaClass)
}