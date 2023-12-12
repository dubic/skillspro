package me.skillspro.projects.models

import me.skillspro.auth.models.User
import me.skillspro.profile.models.Skills

data class Project(val owner: User,
                   val title: Title,
                   val description: String?,
                   val skills: Skills) {
}