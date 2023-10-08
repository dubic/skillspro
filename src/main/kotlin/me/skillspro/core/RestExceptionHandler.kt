package me.skillspro.core

import org.slf4j.LoggerFactory
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class RestExceptionHandler {
private val logger = LoggerFactory.getLogger(javaClass)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleNotFoundException(e: IllegalArgumentException): ProblemDetail {
        logger.error(e.message)
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
    }
}