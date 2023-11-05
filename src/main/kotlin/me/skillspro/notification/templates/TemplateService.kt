package me.skillspro.notification.templates

import org.apache.commons.text.StringSubstitutor
import org.springframework.stereotype.Service


@Service
class TemplateService {

    val tplMap = mapOf("mail.verification" to Templates.accountVerification)

    fun resolveToString(template: String, parameters: Map<String, Any>): String {
        val sub = StringSubstitutor(parameters)
        return sub.replace(tplMap[template])
    }
}