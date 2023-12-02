package me.skillspro.profile

import me.skillspro.auth.models.Email
import me.skillspro.auth.models.User
import me.skillspro.profile.dao.DbProfile
import me.skillspro.profile.dao.ProfileRepo
import me.skillspro.profile.events.SkillsAddedEvent
import me.skillspro.profile.models.Profile
import me.skillspro.profile.models.Skill
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProfileService(private val profileRepo: ProfileRepo,
                     private val events: ApplicationEventPublisher) {
    private val log = LoggerFactory.getLogger(javaClass)
    fun addSkills(newSkills: Set<Skill>, principal: User) {
        log.debug("[{}] adding skills {}", principal.email.value, newSkills)
        val dbProfile = this.getDBProfile(principal.email)
        val oldSkills = dbProfile.skillsValues()
        dbProfile.addToSkills(newSkills)
        this.profileRepo.save(dbProfile)
        log.debug("[{}] added to skills {}", principal.email.value, newSkills)
        events.publishEvent(SkillsAddedEvent(Profile.from(dbProfile), oldSkills, newSkills))
    }

    private fun getDBProfile(email: Email): DbProfile {
        return profileRepo.findByIdOrNull(email.value)
                ?: DbProfile(email.value, mutableSetOf())
    }
}