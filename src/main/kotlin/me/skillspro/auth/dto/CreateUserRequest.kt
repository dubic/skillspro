package me.skillspro.auth.dto

import me.skillspro.core.Data

data class CreateUserRequest(val name: String, val email: String, val password: String): Data()
