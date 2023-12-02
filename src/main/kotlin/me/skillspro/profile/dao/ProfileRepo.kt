package me.skillspro.profile.dao

import org.springframework.data.mongodb.repository.MongoRepository

interface ProfileRepo: MongoRepository<DbProfile, String> {
}