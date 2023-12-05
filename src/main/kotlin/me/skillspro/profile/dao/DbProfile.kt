package me.skillspro.profile.dao

import me.skillspro.profile.models.Skills
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class DbProfile (@field:Id var email: String,
                 var skills: MutableSet<String>) {
    fun addToSkills(newSkills: Skills) {
        newSkills.set().forEach { skills.add(it) }
    }

    fun skillsValues(): Skills {
        return Skills(skills.map { it }.toSet())
    }

}