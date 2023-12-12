package me.skillspro.projects.models

import me.skillspro.core.Validations

class Title(value: String) {
    val value: String
    init {
        Validations.between(value, 2, 100, "title must be between 2 and 100 characters: $value")
        this.value = value.trim()
    }

    override fun toString(): String {
        return "Title(value='$value')"
    }


}