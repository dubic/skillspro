package me.skillspro.auth.dto

data class CreateUserRequest(val name: String, val email: String, val password: String)
