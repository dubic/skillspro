package me.skillspro.profile

import me.skillspro.core.BaseController
import me.skillspro.profile.data.ProfileDetails
import me.skillspro.profile.models.Skills
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
class ProfileController(private val profileService: ProfileService) : BaseController() {

    @PostMapping("/skills/add")
    fun addSkills(@RequestBody skillNames: List<String>): ResponseEntity<Any> {
        if (skillNames.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        this.profileService.addSkills(Skills(skillNames), principal())
        return ResponseEntity.ok().build()
    }

    @PostMapping
    fun addDetails(@RequestBody profileDetails: ProfileDetails): ResponseEntity<Any> {
        this.profileService.saveDetails(profileDetails, principal())
        return ResponseEntity.ok().build()
    }
}