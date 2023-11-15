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
        if (timeWindowCount >= getMaxRequests(request)) {
            block(request)
            return false
        }
        return true
    }

    private fun block(request: RateLimitRequest) {
        val key = getBlockKey(request)
        this.redisRateLimiterStorage.block(key, getTimeWindowBlockedSeconds(request))
        log.warn("blocked for {}: [{}]", request.limitFor, request.ip)
    }

    private fun getBlockKey(request: RateLimitRequest) = "rate_${request.limitFor.lowercase()}_blocked_${request.ip}"

    private fun incrementTimeWindowCounter(request: RateLimitRequest): Long {
        val key = "rate_${request.limitFor.lowercase()}_counter_${request.ip}"
        val current = redisRateLimiterStorage.incrementAndExpire(key,
                getTimeWindowCounterSeconds(request))
        log.debug("Value $key ::: $current")
        return current
    }

    private fun isIpBlocked(request: RateLimitRequest): Boolean {
        val key = getBlockKey(request)
        val blockedStatus = this.redisRateLimiterStorage.getBlockedStratus(key)
        return blockedStatus != null
    }

    private fun getTimeWindowBlockedSeconds(request: RateLimitRequest): Long {
        if (request.isLogin()) {
            return configProperties.rate.loginTimeWindowBlockedSecs
        }
        log.warn("Rate limiter type not detected. returning blocked login config for {}", request
                .limitFor)
        return configProperties.rate.loginTimeWindowBlockedSecs
    }

    private fun getTimeWindowCounterSeconds(request: RateLimitRequest): Long {
        if (request.isLogin()) {
            return configProperties.rate.loginTimeWindowSecs
        }
        log.warn("Rate limiter type not detected. returning counter login config for {}", request
                .limitFor)
        return configProperties.rate.loginTimeWindowSecs
    }

    private fun getMaxRequests(request: RateLimitRequest): Long {
        if (request.isLogin()) {
            return configProperties.rate.loginMaxRequests
        }
        log.warn("Rate limiter type not detected. returning max requests login config for {}",
                request.limitFor)
        return configProperties.rate.loginMaxRequests
    }
}