package me.skillspro.profile.events

import me.skillspro.profile.models.Profile
import me.skillspro.profile.models.Skills

data class SkillsAddedEvent(val profile: Profile, val oldSkills: Skills, val newSkills: Skills) {
    fun isFirstTime() = oldSkills.isEmpty() && !newSkills.isEmpty()
}