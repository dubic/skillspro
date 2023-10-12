package me.skillspro.core.data

class NotificationEvent(val recipient: String, val topic: String, val dataMap: Map<String, Any>)