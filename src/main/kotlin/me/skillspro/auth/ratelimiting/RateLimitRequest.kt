package me.skillspro.auth.ratelimiting

data class RateLimitRequest(val ip: String, val limitFor: String) {
    fun isLogin(): Boolean {
        return limitFor.matches("/auth/login".toRegex())
    }
}
