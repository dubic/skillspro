package me.skillspro.auth.models

import me.skillspro.core.Validations.Companion.between

class Password(password: String) {
    val plain: String = password

    init {
        between(password, 8, 100, "password must be between 8 and 100 characters")
    }
}
