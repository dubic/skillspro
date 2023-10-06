package me.skillspro.auth.dao

import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepo : MongoRepository<DBUser, String> {
}