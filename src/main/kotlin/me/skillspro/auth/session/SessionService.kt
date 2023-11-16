package me.skillspro.auth.session

import me.skillspro.auth.models.User

interface SessionService {
    fun createSession(token: String, user: User)

    fun userInSession(token: String): User?
    fun deleteSession(token: String)
}