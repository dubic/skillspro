package me.skillspro.auth

import me.skillspro.auth.dto.AuthResponse
import me.skillspro.auth.dto.CreateUserRequest
import me.skillspro.auth.dto.CreateUserResponse
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Name
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.auth.tokens.models.Token
import me.skillspro.auth.verification.AccountVerificationService
import me.skillspro.auth.verification.EmailVerificationRequest
import me.skillspro.core.BaseController
import me.skillspro.core.config.ConfigProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService,
                     private val authService: AuthService,
                     private val accountVerificationService: AccountVerificationService) : BaseController() {
    @PostMapping
    fun createAccount(@RequestBody createUserRequest: CreateUserRequest): ResponseEntity<CreateUserResponse> {
        val user = User(
                Name(createUserRequest.name),
                Email(createUserRequest.email, false)
        )
        val createUserResponse = this.userService.createAccount(user, Password(createUserRequest.password))
        return success(createUserResponse)
    }

    @PostMapping("/verify")
    fun verify(@RequestBody verificationRequest: EmailVerificationRequest): ResponseEntity<AuthResponse> {
        val email = Email(verificationRequest.userId, null)
        accountVerificationService.verifyEmail(email, Token(verificationRequest.token))
        val authResponse = authService.authenticate(email)
        return success(authResponse)
    }

    @GetMapping("/verify/resend/{email}")
    fun resendVerificationToken(@PathVariable("email") email: String): ResponseEntity<Unit> {
        this.accountVerificationService.resendVerification(Email(email, null))
        return ResponseEntity.ok().build()
    }
}