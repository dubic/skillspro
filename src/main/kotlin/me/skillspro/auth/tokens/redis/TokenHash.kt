package me.skillspro.auth.tokens.redis

import me.skillspro.auth.tokens.models.TokenType
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash("tokens")
data class TokenHash(
        var token: String,
        var user: String,
        var type: String,
        @field:TimeToLive
        var ttl: Long) {

    @field:Id
    var id: String = createId(user, type)

    init {
        println("TOKEN ID ::: $id")
    }

    companion object {
        fun createId(userId: String, type: String): String {
            return "${userId}_$type"
        }
    }
}