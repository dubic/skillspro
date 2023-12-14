package me.skillspro.projects.data

class ProjectDto(val id: String?,
                 val title: String,
                 val description: String?,
                 val primaryImage: String,
                 val images: List<String>) {
}