package me.skillspro.auth.engage.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class Engagement(
        @field:Id var email: String,
        var verified: Boolean = false,
        var image: Boolean = false,
        var skills: LocalDateTime? = null,
        var projects: LocalDateTime? = null
)