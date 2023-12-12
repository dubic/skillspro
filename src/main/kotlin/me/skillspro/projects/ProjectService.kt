package me.skillspro.projects

import me.skillspro.auth.models.User
import me.skillspro.core.storage.StorageService
import me.skillspro.projects.data.ProjectDto
import me.skillspro.projects.events.ProjectAddedEvent
import me.skillspro.projects.models.DBProject
import me.skillspro.projects.models.Project
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ProjectService(private val storageService: StorageService,
                     private val repo: ProjectRepo,
                     private val events: ApplicationEventPublisher) {
    fun add(project: Project, primary: MultipartFile, images: List<MultipartFile>): ProjectDto {
        log.debug("Adding a project: {}, :: {} :: {}", project, primary.originalFilename, images
                .map { it.originalFilename })
        val primaryUrl = this.storageService.store(primary, "project")
        val imageUrls = images.map { this.storageService.store(it, "project") }
        val savedProject = this.repo.save(DBProject(null, project.owner.email.value, project.title.value, project
                .description, primaryUrl, imageUrls))
        events.publishEvent(ProjectAddedEvent(project))
        log.info("{} Added a project :: {}", project.owner.email.value, project.title)
        return savedProject.toDto()
    }

    private val log = LoggerFactory.getLogger(javaClass)
}