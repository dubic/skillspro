package me.skillspro.projects.models

import me.skillspro.projects.data.ProjectDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("projects")
class DBProject(
        @field:Id var id: String?,
        var email: String,
        var title: String,
        var desc: String?,
        var primaryUrl: String,
        var images: List<String>) {
    fun toDto() = ProjectDto(id,email,title,desc,primaryUrl,images)

}