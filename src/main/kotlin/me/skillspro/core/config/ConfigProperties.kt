package me.skillspro.core.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("sp")
class ConfigProperties {
    val mongo = Mongo()
    var redisSessionTtlSecs: Long = 100
    val redis = Redis()

    class Redis {
        var host: String = ""
        var port = 0
    }

    class Mongo{
        var dbName = "skillspro"
        var url = ""
    }
}
