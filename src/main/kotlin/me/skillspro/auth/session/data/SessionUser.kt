package me.skillspro.auth.session.data

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash("Session")
class SessionUser (
        @field:Id val token: String,
        val name: String,
        val email: String,
        val verified: Boolean,
        @field:TimeToLive val ttl: Long)