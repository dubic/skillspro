package me.skillspro.auth.tokens.models

import me.skillspro.core.Validations.Companion.notEmpty

class Token (val value: String) {
    init {
        notEmpty(value, "Token is required")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Token

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }


}