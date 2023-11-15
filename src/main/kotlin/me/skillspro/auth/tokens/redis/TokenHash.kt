package me.skillspro.auth.tokens.redis

data class TokenHash(
        var token: String,
        var user: String,
        var type: String) {
    fun toMap(): MutableMap<String, String> =
            mutableMapOf("token" to token, "user" to user, "type" to type)

    var id: String = createId(user, type)

    companion object {
        fun createId(userId: String, type: String): String {
            return "tokens:${userId}_$type"
        }

        fun fromMap(map: Map<String, String>, ttl: Long): TokenHash? {
            if(map["token"] ==  null){
                return null
            }
            return TokenHash(map["token"]!!, map["user"]!!, map["type"]!!)
        }
    }
}