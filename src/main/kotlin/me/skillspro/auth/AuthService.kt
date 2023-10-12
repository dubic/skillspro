package me.skillspro.auth

import me.skillspro.auth.dao.UserRepo
import me.skillspro.auth.dto.Account
import me.skillspro.auth.dto.AuthResponse
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.auth.session.SessionService
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(private val passwordService: PasswordService,
                  private val sessionService: SessionService,
                  private val userRepo: UserRepo) {
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

        if (!passwordService.compare(password, dbUser.password)) {
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

    private val logger = LoggerFactory.getLogger(javaClass)
}