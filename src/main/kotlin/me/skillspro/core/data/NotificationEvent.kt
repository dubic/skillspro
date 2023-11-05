package me.skillspro.core.data

class NotificationEvent(
        val recipient: String,
        val template: String,
        val subject: String,
        val dataMap: Map<String, Any>)