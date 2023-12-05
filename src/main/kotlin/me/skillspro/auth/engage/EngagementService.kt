package me.skillspro.auth.engage

import me.skillspro.auth.engage.data.Engagement
import me.skillspro.auth.engage.data.ProfilePhotoEvent
import me.skillspro.auth.models.User
import me.skillspro.auth.verification.AccountVerifiedEvent
import me.skillspro.profile.events.SkillsAddedEvent
import me.skillspro.profile.models.Profile
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

    @Async
    @EventListener
    fun onProfileImage(event: ProfilePhotoEvent) {
        logger.info("Event :: Profile image :: engagement :: ${event.user.email.value}")
        this.profileImageEngagement(event.user)
    }

    @Async
    @EventListener
    fun onSkillsAdded(skillsAddedEvent: SkillsAddedEvent) {
        logger.info("Event :: skills added :: engagement :: {}. Old: {}, New: {}",
                skillsAddedEvent.profile.email.value, skillsAddedEvent.oldSkills,
                skillsAddedEvent.newSkills)
        if (skillsAddedEvent.isFirstTime()) {
            this.skillsAdded(skillsAddedEvent.profile)
        }
    }

    private fun profileImageEngagement(user: User) {
        val engagement = this.engagementRepo.findByIdOrNull(user.email.value)
                ?: Engagement(user.email.value)
        engagement.image = true
        engagementRepo.save(engagement)
        logger.info("Profile image engagement [image=${engagement.image}] saved for" +
                " : {}", user.email.value)
    }

    private fun skillsAdded(profile: Profile) {
        val engagement = this.engagementRepo.findByIdOrNull(profile.email.value)
                ?: Engagement(profile.email.value)
        engagement.skills = true
        this.engagementRepo.save(engagement)
        logger.info("account engagement [skills=${engagement.skills}] saved for : ${
            profile.email
                    .value
        }")
    }

    private fun accountVerifiedEngagement(user: User) {
        val engagement = this.engagementRepo.findByIdOrNull(user.email.value)
                ?: Engagement(user.email.value)
        engagement.verified = true
        this.engagementRepo.save(engagement)
        logger.info("account engagement [verified=${engagement.verified}] saved for : ${
            user.email
                    .value
        }")
    }

    private fun accountCreatedEngagement(user: User) {
        this.engagementRepo.save(Engagement(user.email.value, user.isVerified()))
        logger.info("user created engagement saved for : ${user.email.value}")
    }

    fun getUserEngagement(email: String): Engagement? = this.engagementRepo.findByIdOrNull(email)

}