package me.skillspro.auth.models

import me.skillspro.auth.dao.DBUser

class User(val name: Name, val email: Email, val photoUrl: String?){

    fun isVerified(): Boolean {
        return email.verified ?: false
    }

    companion object {
        fun from(dbUser: DBUser): User {
            return User(Name(dbUser.name), Email(dbUser.email, dbUser.emailVerified), dbUser.photo)
        }
    }
}