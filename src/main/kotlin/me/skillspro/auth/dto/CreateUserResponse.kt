package me.skillspro.auth.dto

data class CreateUserResponse(val verified: Boolean, val tokenTtlSecs: Long)
