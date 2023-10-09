package me.skillspro.auth.engage

import me.skillspro.auth.engage.data.Engagement
import me.skillspro.core.BaseController
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/engagements")
@Profile("e2e")
class EngagementController(private val engagementService: EngagementService) : BaseController() {
    @GetMapping("/user/{email}")
    fun getUserEngagement(@PathVariable("email") email: String): ResponseEntity<Engagement> {
        val engagement = engagementService.getUserEngagement(email)
        return if (engagement != null) {
            success(engagement)
        } else {
            notFound("Engagement not found for $email")
        }
    }


}