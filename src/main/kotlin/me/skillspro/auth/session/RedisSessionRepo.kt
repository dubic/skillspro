package me.skillspro.auth.session

import me.skillspro.auth.session.data.SessionUser
import me.skillspro.auth.tokens.redis.TokenHash
import org.springframework.stereotype.Service
import redis.clients.jedis.JedisPool

@Service
class RedisSessionRepo(private val jedisPool: JedisPool) {
    fun save(sessionUser: SessionUser, ttl: Long) {
        jedisPool.resource.use {
            it.hset(sessionUser.id(), sessionUser.toMap())
            it.expire(sessionUser.id(), ttl)
        }
    }

    fun findById(token: String): SessionUser? {
        jedisPool.resource.use {
            val id = SessionUser.id(token)
            val map = it.hgetAll(id)
            val ttl = it.ttl(id)
            return SessionUser.fromMap(map, ttl)
        }
    }

    fun delete(token: String) {
        jedisPool.resource.use {
            it.del(SessionUser.id(token))
        }
    }

    fun extendExpiresWith(token: String, ttl: Long) {
        jedisPool.resource.use {
            it.expire(SessionUser.id(token), ttl)
        }
    }
}