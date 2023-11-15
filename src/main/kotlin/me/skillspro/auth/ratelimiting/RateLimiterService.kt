package me.skillspro.auth.ratelimiting

import me.skillspro.core.config.ConfigProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RateLimiterService(private val redisRateLimiterStorage: RedisRateLimiterStorage,
                         private val configProperties: ConfigProperties) {
    private val log = LoggerFactory.getLogger(javaClass)
    fun canProceed(request: RateLimitRequest): Boolean {
        val ipBlocked = isIpBlocked(request)
        if (ipBlocked) {
            return false
        }
        val timeWindowCount = incrementTimeWindowCounter(request)
        if (timeWindowCount >= request.getMaxRequests(configProperties)) {
            block(request)
            return false
        }
        return true
    }

    private fun block(request: RateLimitRequest) {
        val key = getBlockKey(request)
        this.redisRateLimiterStorage.block(key, request.getTimeWindowBlockedSeconds(configProperties))
        log.warn("blocked for {}: [{}]", request.type, request.ip)
    }

    private fun getBlockKey(request: RateLimitRequest)
    = "rate_${request.type.name.lowercase()}_blocked_${request.ip}"

    private fun incrementTimeWindowCounter(request: RateLimitRequest): Long {
        val key = "rate_${request.type.name.lowercase()}_counter_${request.ip}"
        val current = redisRateLimiterStorage.incrementAndExpire(key,
                request.getTimeWindowCounterSeconds(configProperties))
        log.debug("Value $key ::: $current")
        return current
    }

    private fun isIpBlocked(request: RateLimitRequest): Boolean {
        val key = getBlockKey(request)
        val blockedStatus = this.redisRateLimiterStorage.getBlockedStratus(key)
        return blockedStatus != null
    }
}