package me.skillspro.auth.session.data

import me.skillspro.auth.tokens.redis.TokenHash

class SessionUser(
        var token: String,
        var name: String,
        var email: String,
        var verified: Boolean) {
    fun toMap(): MutableMap<String, String> =
            mutableMapOf("token" to token, "name" to name, "email" to email, "verified" to
                    verified.toString())

    fun id(): String = Companion.id(token)
    companion object {
        fun id(token: String): String = "sessions:$token"

        fun fromMap(map: Map<String, String>, ttl: Long): SessionUser? {
            if(map["token"] ==  null){
                return null
            }
            return SessionUser(map["token"]!!, map["name"]!!, map["email"]!!, map["verified"]!!
                    .toBoolean())
        }
    }
}