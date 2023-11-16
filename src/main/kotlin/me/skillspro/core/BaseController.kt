package me.skillspro.core

import me.skillspro.auth.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder

abstract class BaseController {
    fun <T> success(payload: T?): ResponseEntity<T> {
        return ResponseEntity.ok(payload)
    }

    fun <T> notFound(msg: String?): ResponseEntity<T> {
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, msg.toString()))
                .build()
    }

    fun principal(): User {
        return SecurityContextHolder.getContext().authentication.principal as User
    }

    fun getCredentials(): String{
        return SecurityContextHolder.getContext().authentication.credentials as String
    }
}