package me.skillspro.auth.models

import me.skillspro.core.Validations.Companion.between
import me.skillspro.core.Validations.Companion.onlyAlphabets

class Name(value: String) {
    val value: String

    init {
        between(value, 3, 70, "name must be between 3 and 70 characters: $value")
        onlyAlphabets(value, "name must be alphabets only: $value")
        this.value = value.trim()
    }
}