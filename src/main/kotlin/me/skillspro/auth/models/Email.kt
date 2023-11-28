package me.skillspro.auth.models

import me.skillspro.core.Validations.Companion.notEmpty
import me.skillspro.core.Validations.Companion.validMail

class Email(email: String, val verified: Boolean?) {
    val value: String = email.trim().lowercase()

    init {
        notEmpty(email, "email must be provided")
        validMail(email.trim(), "email is invalid")
    }

    fun isVerified(): Boolean {
        return this.verified ?: false
    }

    fun name(): String {
        return this.value.substringBefore("@")
    }
}
