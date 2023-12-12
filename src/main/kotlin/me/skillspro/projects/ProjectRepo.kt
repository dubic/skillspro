package me.skillspro.projects

import me.skillspro.projects.models.DBProject
import org.springframework.data.mongodb.repository.MongoRepository

interface ProjectRepo : MongoRepository<DBProject, String> {
}