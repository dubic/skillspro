package me.skillspro.notification

import me.skillspro.core.data.NotificationEvent
import me.skillspro.notification.mail.IMailService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("e2e")
class LoggerEmailService : IMailService{
    private val logger = LoggerFactory.getLogger(javaClass)
    private val map: MutableMap<String, String> = mutableMapOf()
    override fun send(notification: NotificationEvent) {
        logger.warn("please delete - code :: [${notification.dataMap["token"]}]")
        this.map[notification.recipient] = notification.dataMap["token"] as String
    }

    fun getUserVerificationToken(email: String): String {
        return map[email] ?: "invalid-278"
    }
}