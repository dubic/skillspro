package me.skillspro.auth

import me.skillspro.auth.dao.DBUser
import me.skillspro.auth.dao.UserRepo
import me.skillspro.auth.dto.CreateUserResponse
import me.skillspro.auth.dto.SocialLoginRequest
import me.skillspro.auth.engage.data.ProfilePhotoEvent
import me.skillspro.auth.firebase.FirebaseService
import me.skillspro.auth.firebase.GoogleAccount
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.auth.tokens.TokenService
import me.skillspro.auth.tokens.models.Token
import me.skillspro.auth.tokens.models.TokenRequest
import me.skillspro.auth.tokens.models.TokenType
import me.skillspro.auth.verification.AccountVerifiedEvent
import me.skillspro.core.config.ConfigProperties
import me.skillspro.core.data.NotificationEvent
import me.skillspro.core.storage.StorageService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(private val userRepo: UserRepo,
                  private val configProperties: ConfigProperties,
                  private val events: ApplicationEventPublisher,
                  private val passwordEncoder: PasswordEncoder,
                  private val tokenService: TokenService,
                  private val firebaseService: FirebaseService,
                  private val storageService: StorageService) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private fun accountDoesNotExist(email: Email) {
        val users = this.userRepo.findByIdOrNull(email.value)
        if (users != null) {
            this.logger.warn("user ${email.value} already exists")
            throw IllegalArgumentException("user ${email.value} already exists")
        }
    }

    private fun doCreateAccount(user: User, password: Password) {
        val hashed = this.hashPassword(password)
        this.userRepo.save(DBUser(user.name.value, user.email.value, hashed, user.isVerified(),
                user.photoUrl))
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
                ?: throw NoSuchElementException("Account not found: " + email.value)
        return User.from(dbUser)
    }

    fun findDbUser(email: Email): DBUser {
        return userRepo.findByIdOrNull(email.value)
                ?: throw NoSuchElementException("Account not found: " + email.value)
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
            return this.saveGoogleAccountDetails(dbUser, googleAccount)
        }
        //create profile
        dbUser = userRepo.save(DBUser(googleAccount.displayName, googleAccount.email, null,
                true, googleAccount.photoUrl))
        val user = User.from(dbUser)
        this.events.publishEvent(user)
        return user
    }

    private fun saveGoogleAccountDetails(dbUser: DBUser, googleAccount: GoogleAccount): User {
        if (dbUser.photo == null) {
            dbUser.photo = googleAccount.photoUrl
            this.userRepo.save(dbUser)
            this.events.publishEvent(ProfilePhotoEvent(User.from(dbUser)))
        }
        return User.from(dbUser)
    }

    fun hashPassword(password: Password): String {
        return passwordEncoder.encode(password.plain)
    }

    fun comparePassword(password: Password, hash: String): Boolean {
        return passwordEncoder.matches(password.plain, hash)
    }

    fun forgotPassword(email: Email) {
        logger.debug("Attempting password reset :: {}", email.value)
        val user = this.findAccount(email)
        val createdToken = tokenService.createToken(TokenRequest(email, TokenType.PASSWORD))
        this.events.publishEvent(
                NotificationEvent(user.email.value, "mail.password", configProperties
                        .passwordForgotSubject,
                        mapOf("token" to createdToken.value,
                                "name" to user.name.value))
        )
    }

    fun verifyTokenAndResetPassword(email: Email, newPassword: Password, plainToken: Token) {
        logger.debug("Starting password reset process :: {}", email.value)
        val tokenValid = this.tokenService.isTokenValid(email, plainToken, TokenType.PASSWORD)
        if (!tokenValid) {
            throw java.lang.IllegalArgumentException("Password reset token is invalid: " + email.value)
        }
        this.resetPassword(email, newPassword)
        tokenService.deleteExistingToken(email.value, TokenType.PASSWORD)
    }

    private fun resetPassword(email: Email, newPassword: Password) {
        val hashedNewPassword = this.hashPassword(newPassword)
        val dbUser = this.userRepo.findByIdOrNull(email.value)
                ?: throw NoSuchElementException("Account not found: " + email.value)
        dbUser.password = hashedNewPassword
        userRepo.save(dbUser)
        logger.info("Password reset successful: {}", email.value)
    }

    fun addProfilePhoto(photo: MultipartFile, principal: User): String {
        val dbUser = this.findDbUser(principal.email)
        this.storageService.delete(dbUser.photo?.substringAfterLast("/"))
        val imageUrl = this.storageService.store(photo, principal.email.name())
        dbUser.photo = imageUrl
        this.userRepo.save(dbUser)
        this.events.publishEvent(ProfilePhotoEvent(principal))
        return imageUrl
    }
}