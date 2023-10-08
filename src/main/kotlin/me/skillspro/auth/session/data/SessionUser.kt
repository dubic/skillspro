package me.skillspro.auth.session.data

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash("Session")
class SessionUser (
        @field:Id var token: String,
        var name: String,
        var email: String,
        var verified: Boolean,
        @field:TimeToLive var ttl: Long)