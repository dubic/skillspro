package me.skillspro.auth.ratelimiting

import org.springframework.stereotype.Service
import redis.clients.jedis.JedisPool
import redis.clients.jedis.args.ExpiryOption

@Service
class RedisRateLimiterStorage(private val jedisPool: JedisPool) {

    fun getBlockedStratus(key: String): String? {
        jedisPool.resource.use {
            return it.get(key)
        }
    }

    fun incrementAndExpire(key: String, timeWindowSeconds: Long): Long {
        jedisPool.resource.use {
            val transaction = it.multi()
            val incrResponse = transaction.incr(key)
            transaction.expire(key, timeWindowSeconds, ExpiryOption.NX)
            transaction.exec()
            return incrResponse.get()
        }
    }

    fun block(key: String, timeWindowBlockedSecs: Long) {
        jedisPool.resource.use {
            it.set(key, true.toString())
            it.expire(key, timeWindowBlockedSecs, ExpiryOption.NX)
        }
    }
}