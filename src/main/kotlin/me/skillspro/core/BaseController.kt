package me.skillspro.core

import org.springframework.http.ResponseEntity

abstract class BaseController {
    fun <T> success(payload: T?): ResponseEntity<T> {
        return ResponseEntity.ok(payload)
    }
}