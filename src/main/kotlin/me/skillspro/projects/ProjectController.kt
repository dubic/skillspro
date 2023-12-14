package me.skillspro.projects

import me.skillspro.core.BaseController
import me.skillspro.profile.models.Skills
import me.skillspro.projects.data.ProjectData
import me.skillspro.projects.data.ProjectDto
import me.skillspro.projects.models.Project
import me.skillspro.projects.models.Title
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) : BaseController() {
    @PostMapping
    fun addSkills(@RequestPart("primaryImage", required = true) primary: MultipartFile,
                  @RequestPart("image2") image2: MultipartFile?,
                  @RequestPart("image3") image3: MultipartFile?,
                  @RequestPart("project") p: ProjectData):
            ResponseEntity<ProjectDto> {
        val project = Project(principal(), Title(p.title), p.description, Skills(p.skills))

        val savedProject = this.projectService.add(project, primary, listOfNotNull(image2, image3))

        return ResponseEntity.ok(savedProject)
    }

    @GetMapping
    fun myProjects(@RequestParam("size", defaultValue = "10") size: Int): ResponseEntity<Any> {
        val projects = this.projectService.load(principal(), size)
        return ResponseEntity.ok(projects)
    }
}