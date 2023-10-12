package me.skillspro.notification

import me.skillspro.auth.engage.data.Engagement
import me.skillspro.core.BaseController
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notification")
@Profile("e2e")
class NotificationController(private val emailService: EmailService) : BaseController() {
    @GetMapping("/token/{email}")
    fun getUserVerificationToken(@PathVariable("email") email: String):
            ResponseEntity<Map<String,String>> =
            success(mapOf("token" to emailService.getUserVerificationToken(email)))


}