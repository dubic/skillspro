package me.skillspro.auth

import me.skillspro.auth.dto.AuthResponse
import me.skillspro.auth.dto.LoginRequest
import me.skillspro.auth.dto.SocialLoginRequest
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Password
import me.skillspro.core.APIResponse
import me.skillspro.core.BaseController
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) :
        BaseController() {

    private val logger = LoggerFactory.getLogger(javaClass)
    @GetMapping("/valid/{token}")
    fun validAuthToken(@PathVariable token: String): ResponseEntity<APIResponse<Any?>> {
        if (!this.authService.isAuthenticationValid(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
        return ResponseEntity.ok().build()
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        val authResponse = this.authService.login(
                Email(loginRequest.email, null),
                Password(loginRequest.password))
        return success(authResponse)
    }

    @GetMapping("/logout")
    fun logout(): ResponseEntity<Any> {
        this.authService.logout(getCredentials())
        logger.info("User logged out [{}]", principal().email.value)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/social/login")
    fun loginSocial(@RequestBody socialLoginRequest: SocialLoginRequest): ResponseEntity<AuthResponse> =
            success(authService.loginSocial(socialLoginRequest))
}