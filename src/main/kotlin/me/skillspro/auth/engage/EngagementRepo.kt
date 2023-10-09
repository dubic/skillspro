package me.skillspro.auth.engage

import me.skillspro.auth.engage.data.Engagement
import org.springframework.data.mongodb.repository.MongoRepository

interface EngagementRepo : MongoRepository<Engagement, String> {
}