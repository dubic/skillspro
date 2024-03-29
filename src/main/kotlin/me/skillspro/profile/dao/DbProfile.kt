package me.skillspro.profile.dao

import me.skillspro.core.data.Audited
import me.skillspro.profile.models.Skills
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class DbProfile(@field:Id var email: String,
                var phone: String?,
                var ig: String?,
                var location: String?,
                var skills: MutableSet<String>): Audited() {
    fun addToSkills(newSkills: Skills) {
        newSkills.set().forEach { skills.add(it) }
    }

    fun skillsValues(): Skills {
        return Skills(skills.map { it }.toSet())
    }

}