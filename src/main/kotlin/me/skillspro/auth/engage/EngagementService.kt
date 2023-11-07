package me.skillspro.auth.engage

import me.skillspro.auth.engage.data.Engagement
import me.skillspro.auth.models.User
import me.skillspro.auth.verification.AccountVerifiedEvent
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
    fun onUserCreated(user: User) {
        logger.info("Event :: user created :: engagement :: ${user.email.value}")
        this.accountCreatedEngagement(user)
    }

    @Async
    @EventListener
    fun onAccountVerified(accountVerifiedEvent: AccountVerifiedEvent) {
        logger.info("Event :: account verified :: engagement :: ${accountVerifiedEvent.user.email}")
        this.accountVerifiedEngagement(accountVerifiedEvent.user)
    }

    private fun accountVerifiedEngagement(user: User) {
        val engagement = this.engagementRepo.findByIdOrNull(user.email.value)
                ?: Engagement(user.email.value)
        engagement.verified = true
        this.engagementRepo.save(engagement)
        logger.info("account engagement [verified=${engagement.verified}] saved for : ${user.email
                .value}")
    }

    private fun accountCreatedEngagement(user: User) {
        this.engagementRepo.save(Engagement(user.email.value, user.isVerified()))
        logger.info("user created engagement saved for : ${user.email.value}")
    }

    fun getUserEngagement(email: String): Engagement? = this.engagementRepo.findByIdOrNull(email)

}