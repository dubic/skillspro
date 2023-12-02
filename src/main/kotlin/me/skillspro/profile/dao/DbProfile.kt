package me.skillspro.profile.dao

import me.skillspro.profile.models.Skill
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class DbProfile (@field:Id var email: String,
                 var skills: MutableSet<String>) {
    fun addToSkills(newSkills: Set<Skill>) {
        newSkills.forEach { skills.add(it.value) }
    }

    fun skillsValues(): Set<Skill> {
        return this.skills.map { Skill(it) }.toSet()
    }

}