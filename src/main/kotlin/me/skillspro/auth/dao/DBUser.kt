package me.skillspro.auth.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class DBUser(@field:Id var name: String, var email: String, var password: String, var emailVerified:
Boolean) {

}