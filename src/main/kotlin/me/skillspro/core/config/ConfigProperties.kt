package me.skillspro.core.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("sp")
class ConfigProperties {
    val mail = Mail()
    val mongo = Mongo()
    var redisSessionTtlSecs: Long = 100
    var redisTokenTtlSecs: Long = 100
    var emailVerificationSubject: String = ""
    val redis = Redis()

    class Redis {
        var host: String = ""
        var port = 0
    }

    class Mongo{
        var dbName = "skillspro"
        var url = ""
    }

    class Mail{
        var sender = "dubisoft.tech@gmail.com"
        var senderName = "Dubisoft Tech"
    }
}
