package me.skillspro.profile.models

import me.skillspro.auth.models.Email
import me.skillspro.profile.dao.DbProfile

class Profile(val email: Email, val skills: Skills) {


    companion object {
        fun from(db: DbProfile): Profile {
            return Profile(Email(db.email, true), Skills(db.skills))
        }
    }
}