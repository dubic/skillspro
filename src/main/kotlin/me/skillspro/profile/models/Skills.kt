package me.skillspro.profile.models

import me.skillspro.core.Validations
import me.skillspro.core.Validations.Companion.between
import me.skillspro.core.Validations.Companion.onlyAlphabets

data class Skills(val values: Collection<String>) {
    init {
        values.forEach {
            between(it, 2, 50, "skill name must be between 2 and 50 characters: $it")
            onlyAlphabets(it, "skill name must be alphabets only: $it")
        }
    }

    fun set() = values.toSet()

    fun isEmpty() = values.isEmpty()

    fun size() = set().size
}