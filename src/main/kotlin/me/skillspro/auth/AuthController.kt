package me.skillspro.auth

import me.skillspro.auth.dto.AuthResponse
import me.skillspro.auth.dto.LoginRequest
import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Password
import me.skillspro.core.APIResponse
import me.skillspro.core.BaseController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) : BaseController() {
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

    @PostMapping("/logout")
    fun logout(): ResponseEntity<AuthResponse> {
        this.authService.logout()
        return ResponseEntity.ok().build()
    }
}