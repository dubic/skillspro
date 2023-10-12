package me.skillspro.auth

import me.skillspro.auth.dto.CreateUserRequest
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Name
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.auth.tokens.models.Token
import me.skillspro.auth.verification.AccountVerificationService
import me.skillspro.auth.verification.EmailVerificationRequest
import me.skillspro.core.BaseController
import me.skillspro.core.config.ConfigProperties
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService,
                     private val configProperties: ConfigProperties,
                     private val accountVerificationService: AccountVerificationService) : BaseController() {
    @PostMapping
    fun createAccount(@RequestBody createUserRequest: CreateUserRequest): Any {
        val user = User(
                Name(createUserRequest.name),
                Email(createUserRequest.email, false)
        )
        this.userService.createAccount(user, Password(createUserRequest.password))
        return success(object {
            val verified = user.isVerified()
            val tokenTtlSecs = configProperties.redisTokenTtlSecs
        })
    }

    @PostMapping("/verify")
    fun verify(@RequestBody verificationRequest: EmailVerificationRequest): Unit {
        accountVerificationService.verifyEmail(
                Email(verificationRequest.userId, null),
                Token(verificationRequest.token))
    }

}