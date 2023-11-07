package me.skillspro.auth

import me.skillspro.auth.dao.DBUser
import me.skillspro.auth.dao.UserRepo
import me.skillspro.auth.dto.CreateUserResponse
import me.skillspro.auth.dto.SocialLoginRequest
import me.skillspro.auth.firebase.FirebaseService
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Name
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.auth.verification.AccountVerifiedEvent
import me.skillspro.core.config.ConfigProperties
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.IllegalStateException
import java.util.NoSuchElementException

@Service
class UserService(private val userRepo: UserRepo,
                  private val passwordService: PasswordService,
                  private val configProperties: ConfigProperties,
                  private val events: ApplicationEventPublisher,
                  private val firebaseService: FirebaseService) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun accountDoesNotExist(email: Email) {
        val users = this.userRepo.findByIdOrNull(email.value)
        if (users != null) {
            this.logger.warn("user ${email.value} already exists")
            throw IllegalArgumentException("user ${email.value} already exists")
        }
    }

    private fun doCreateAccount(user: User, password: Password) {
        val hashed = this.passwordService.hash(password)
        this.userRepo.save(DBUser(user.name.value, user.email.value, hashed, user.isVerified()))
    }

    fun createAccount(user: User, password: Password): CreateUserResponse {
        this.logger.debug("create account request " + user.email.value)
        this.accountDoesNotExist(user.email)
        this.doCreateAccount(user, password)
        this.events.publishEvent(user)
        this.logger.debug("created account successfully " + user.email.value)
        return CreateUserResponse(user.isVerified(), configProperties.redisTokenTtlSecs)
    }

    fun findAccount(email: Email): User {
        val dbUser = this.userRepo.findByIdOrNull(email.value)
                ?: throw NoSuchElementException("Not found: " + email.value)
        return User.from(dbUser)
    }

    fun validateAccount(email: Email) {
        val foundUser = this.userRepo.findByIdOrNull(email.value)
                ?: throw IllegalStateException("Account not found: ${email.value}")
        foundUser.emailVerified = true
        this.userRepo.save(foundUser)
        this.events.publishEvent(AccountVerifiedEvent(User.from(foundUser)))
        logger.info("Account verified successfully [${email.value}]")
    }

    fun getOrCreateUser(socialLoginRequest: SocialLoginRequest): User {
        val googleAccount = firebaseService.verifyAccount(socialLoginRequest.idToken)
        logger.debug("Account verified: {}", googleAccount)
        var dbUser = userRepo.findByIdOrNull(googleAccount.email)
        if (dbUser != null) {
            return User.from(dbUser)
        }
        //create profile
        dbUser = userRepo.save(DBUser(googleAccount.displayName, googleAccount.email, null, true))
        val user = User.from(dbUser)
        this.events.publishEvent(user)
        return user
    }
}