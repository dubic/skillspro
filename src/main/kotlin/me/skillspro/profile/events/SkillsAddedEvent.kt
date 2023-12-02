package me.skillspro.profile.events

import me.skillspro.profile.models.Profile
import me.skillspro.profile.models.Skill

class SkillsAddedEvent(profile: Profile, oldSkills: Set<Skill>, newSkills: Set<Skill>) {
}