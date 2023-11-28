package me.skillspro.core

import java.util.regex.Pattern

class Validations {
    companion object {

        fun notNull(value: Any?, msg: String?) {
            if (value == null) throw IllegalArgumentException(msg ?: "value cannot be empty")
        }

        fun notEmpty(value: String, msg: String?) {
            if (value.trim().isEmpty()) throw IllegalArgumentException(msg
                    ?: "value cannot be empty")
        }

        fun validMail(value: String, msg: String?) {
            val regex = "^[A-Za-z0-9+_.-]+@(.+)\$"
            val pattern: Pattern = Pattern.compile(regex)
            if (!pattern.matcher(value).matches()) throw IllegalArgumentException(msg
                    ?: "invalid email")
        }

        fun between(value: String, min: Int, max: Int, msg: String?) {
            notEmpty(value, msg)
            if (value.length < min || value.length > max)
                throw IllegalArgumentException(msg ?: "value must be between $min and $max")
        }

        fun onlyAlphabets(inputString: String, msg: String?) {
            val regex = "^[A-Za-z]+(\\s+[A-Za-z]+)?$"
            val pattern: Pattern = Pattern.compile(regex)
            if (!pattern.matcher(inputString).matches()) throw IllegalArgumentException(msg
                    ?: "Only alphabets accepted")
        }

        fun notContained(inputString: String?, options: Array<String>, msg: String?) {
            if (inputString == null){
                return
            }
            if (!options.contains(inputString)) {
                throw IllegalArgumentException(msg ?: "Not found in options")
            }
        }
    }
}