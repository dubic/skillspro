package me.skillspro.profile.models

import me.skillspro.auth.models.Email
import me.skillspro.profile.dao.DbProfile

class Profile(val email: Email, val skills: Set<Skill>) {

    fun addToSkills(newSkills: Set<Skill>) {
        newSkills.forEach { skills.plus(it.value) }
    }
    companion object {
        fun from(db: DbProfile): Profile {
            return Profile(Email(db.email, true), db.skills.map { Skill(it) }.toSet())
        }
    }
}