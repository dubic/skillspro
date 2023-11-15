package me.skillspro.auth.ratelimiting

import me.skillspro.core.config.ConfigProperties

data class RateLimitRequest(val ip: String, val type: RateLimitType) {
    fun getTimeWindowBlockedSeconds(configProperties: ConfigProperties): Long {
        return when (type) {
            RateLimitType.LOGIN -> {
                configProperties.rate.loginTimeWindowBlockedSecs
            }
            RateLimitType.PASSWORD_RESET -> {
                configProperties.rate.passwordResetTimeWindowBlockedSecs
            }
        }
    }

    fun getTimeWindowCounterSeconds(configProperties: ConfigProperties): Long {
        return when (type) {
            RateLimitType.LOGIN -> {
                configProperties.rate.loginTimeWindowSecs
            }
            RateLimitType.PASSWORD_RESET -> {
                configProperties.rate.passwordResetTimeWindowSecs
            }
        }
    }

    fun getMaxRequests(configProperties: ConfigProperties): Long {
        return when (type) {
            RateLimitType.LOGIN -> {
                configProperties.rate.loginMaxRequests
            }
            RateLimitType.PASSWORD_RESET -> {
                configProperties.rate.passwordResetMaxRequests
            }
        }
    }

}
