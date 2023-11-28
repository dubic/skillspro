package me.skillspro.auth

import jakarta.servlet.http.HttpServletRequest
import me.skillspro.auth.dto.AuthResponse
import me.skillspro.auth.dto.CreateUserRequest
import me.skillspro.auth.dto.CreateUserResponse
import me.skillspro.auth.dto.PasswordResetRequest
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Name
import me.skillspro.auth.models.Password
import me.skillspro.auth.models.User
import me.skillspro.auth.tokens.models.Token
import me.skillspro.auth.verification.AccountVerificationService
import me.skillspro.auth.verification.EmailVerificationRequest
import me.skillspro.core.BaseController
import me.skillspro.core.Validations
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService,
                     private val authService: AuthService,
                     private val accountVerificationService: AccountVerificationService) :
        BaseController() {
    @PostMapping
    fun createAccount(@RequestBody createUserRequest: CreateUserRequest): ResponseEntity<CreateUserResponse> {
        val user = User(
                Name(createUserRequest.name),
                Email(createUserRequest.email, false),
                null
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

    @GetMapping("/password/forgot/{email}")
    fun forgotPassword(@PathVariable email: String): ResponseEntity<Any> {
        this.userService.forgotPassword(Email(email, null))
        return ResponseEntity.ok().build()
    }

    @PostMapping("/password/reset")
    fun resetPassword(@RequestBody passwordResetRequest: PasswordResetRequest): ResponseEntity<Any> {
        val email = Email(passwordResetRequest.email, null)
        val password = Password(passwordResetRequest.newPassword)
        val token = Token(passwordResetRequest.token)
        this.userService.verifyTokenAndResetPassword(email, password, token)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/photo")
    fun addProfilePhoto(@RequestParam(name = "profile", required = false) photo:
                        MultipartFile?,
                        request: HttpServletRequest):
            ResponseEntity<String> {
        Validations.notNull(photo, "No photoUrl image found")
        Validations.notContained(photo?.contentType, arrayOf("image/jpeg","image/png"), "png or " +
                "jpeg expected")

        val profileImageUrl = this.userService.addProfilePhoto(photo!!, principal())
        return ResponseEntity.ok(profileImageUrl)
    }
}