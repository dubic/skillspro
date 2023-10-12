package me.skillspro.notification

import me.skillspro.core.data.NotificationEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EmailService {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val map: MutableMap<String, String> = mutableMapOf()
    fun send(notification: NotificationEvent) {
        logger.warn("please delete - code :: [${notification.dataMap["token"]}]")
        this.map[notification.recipient] = notification.dataMap["token"] as String
    }

    fun getUserVerificationToken(email: String): String {
        return map[email] ?: "invalid-278"
    }
}