package me.skillspro.auth

import me.skillspro.auth.models.Password
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordService (private val passwordEncoder: PasswordEncoder){
    fun hash(password: Password): String {
        return passwordEncoder.encode(password.plain)
    }

    fun compare(password: Password, hash: String): Boolean {
        return passwordEncoder.matches(password.plain, hash)
    }
}