package me.skillspro.profile

import me.skillspro.core.BaseController
import me.skillspro.profile.models.Skill
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
class ProfileController(private val profileService: ProfileService) : BaseController() {

    @PostMapping("/skills/add")
    fun addSkills(@RequestBody skills: Set<String>): ResponseEntity<Any> {
        this.profileService.addSkills(skills.map { Skill(it) }.toSet(), principal())
        return ResponseEntity.ok().build()
    }
}