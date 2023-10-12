package me.skillspro.auth.tokens.models

import me.skillspro.core.Validations.Companion.notEmpty

class Token (val value: String) {
    init {
        notEmpty(value, "Token is required")
    }
}