package me.skillspro.notification.mail

import jakarta.mail.internet.MimeMessage
import me.skillspro.core.config.ConfigProperties
import me.skillspro.notification.models.Email
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class SmtpMailEngine(private val sender: JavaMailSender,
                     private val configProperties: ConfigProperties) : MailEngine {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun sendMail(email: Email) {
        val mime: MimeMessage = sender.createMimeMessage()
        val helper = MimeMessageHelper(mime, true)

        helper.setText(email.body, true)
        helper.setTo(email.recipient)
        helper.setSubject(email.subject)
        helper.setFrom(configProperties.mail.sender, configProperties.mail.senderName)

        sender.send(mime)
        logger.info("mail sent successfully [{}], [{}]", email.subject, email.recipient)
    }
}