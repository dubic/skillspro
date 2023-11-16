package me.skillspro.auth.models

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class UserAuthentication(private var authenticated: Boolean,
                         private val token: String,
                         private val user: User) :
        Authentication {

    override fun getName(): String {
        return "Skillspro authentication"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getCredentials(): Any {
        return this.token
    }

    override fun getDetails(): Any {
        return user
    }

    override fun getPrincipal(): Any {
        return user
    }

    override fun isAuthenticated(): Boolean {
        return this.authenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated
    }
}