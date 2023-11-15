package me.skillspro.auth.tokens.redis

import org.springframework.stereotype.Service
import redis.clients.jedis.JedisPool

@Service
class RedisTokenRepo(private val jedisPool: JedisPool) {
    fun findByIdOrNull(id: String): TokenHash? {
        jedisPool.resource.use {
            val map = it.hgetAll(id)
            val ttl = it.ttl(id)
            return TokenHash.fromMap(map, ttl)
        }
    }

    fun delete(tokenHash: TokenHash) {
        jedisPool.resource.use {
            it.del(tokenHash.id)
        }
    }

    fun save(tokenHash: TokenHash, ttl: Long) {
        jedisPool.resource.use {
            it.hset(tokenHash.id, tokenHash.toMap())
            it.expire(tokenHash.id, ttl)
        }
    }
}