package me.skillspro.notification.mail

import me.skillspro.core.data.NotificationEvent
import me.skillspro.notification.models.Email
import me.skillspro.notification.templates.TemplateService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("prod")
class EmailService(private val templateService: TemplateService,
        private val mailEngine: MailEngine) : IMailService{
    override fun send(notification: NotificationEvent) {
        val body = this.templateService.resolveToString(notification.template, notification.dataMap)
        val email = Email(notification.recipient, notification.subject, body)
        mailEngine.sendMail(email)
    }
}