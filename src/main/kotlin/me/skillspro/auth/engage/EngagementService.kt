package me.skillspro.auth.engage

import me.skillspro.auth.engage.data.Engagement
import me.skillspro.auth.models.User
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EngagementService(private val engagementRepo: EngagementRepo) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Async
    @EventListener
    fun userCreated(user: User) {
        logger.info("Event :: user created :: engagement :: ${user.email.value}")
        this.accountCreatedEngagement(user)
    }

    private fun accountCreatedEngagement(user: User) {
        this.engagementRepo.save(Engagement(user.email.value))
        logger.info("user created engagement saved for : ${user.email.value}")
    }

    fun getUserEngagement(email: String): Engagement? = this.engagementRepo.findByIdOrNull(email)

}