package me.skillspro.auth.dto

data class PasswordResetRequest(val email: String, val token: String, val newPassword: String)
