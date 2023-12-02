package me.skillspro.profile.models

import me.skillspro.core.Validations
import me.skillspro.core.Validations.Companion.between

data class Skill(val value: String){
    init {
        between(value, 2, 50, "skill name must be between 2 and 50 characters: $value")
        Validations.onlyAlphabets(value, "skill name must be alphabets only: $value")
    }
}
