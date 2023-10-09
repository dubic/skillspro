package me.skillspro.core

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity

abstract class BaseController {
    fun <T> success(payload: T?): ResponseEntity<T> {
        return ResponseEntity.ok(payload)
    }

    fun <T>notFound(msg: String?): ResponseEntity<T> {
        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, msg.toString()))
                .build()
    }
}