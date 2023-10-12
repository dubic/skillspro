package me.skillspro.notification

import me.skillspro.core.data.NotificationEvent
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class NotificationService(private val emailService: EmailService) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Async
    @EventListener
    fun onNotification(notification: NotificationEvent) {
        logger.info("Event :: notification [${notification.topic} -> ${notification.recipient}]")
        notify(notification)
    }

    private fun notify(notification: NotificationEvent) {
        emailService.send(notification)
    }
}