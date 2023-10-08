package me.skillspro.auth

import me.skillspro.auth.dao.DBUser
import me.skillspro.auth.dao.UserRepo
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Name
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.NoSuchElementException
@Service
class UserService(private val userRepo: UserRepo,
                  private val passwordService: PasswordService,
                  private val events: ApplicationEventPublisher) {
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

    fun createAccount(user: User, password: Password) {
        this.logger.debug("create account request " + user.email.value)
        this.accountDoesNotExist(user.email)
        val createdUser = this.doCreateAccount(user, password)
        this.events.publishEvent(user)
        return createdUser
    }

    fun findAccount(email: Email): User {
        val dbUser = this.userRepo.findByIdOrNull(email.value)
                ?: throw NoSuchElementException("Not found: " + email.value)
        return User.from(dbUser)
    }
}