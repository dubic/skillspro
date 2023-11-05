package me.skillspro.notification.mail

import me.skillspro.notification.models.Email

interface MailEngine {
    fun sendMail(email: Email)
}