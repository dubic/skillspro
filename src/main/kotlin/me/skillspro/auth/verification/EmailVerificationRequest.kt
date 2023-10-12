package me.skillspro.auth.verification

data class EmailVerificationRequest(val userId: String, val token: String)
