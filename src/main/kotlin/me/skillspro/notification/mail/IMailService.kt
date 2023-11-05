package me.skillspro.notification.mail

import me.skillspro.core.data.NotificationEvent

interface IMailService {
    fun send(notification: NotificationEvent)
}