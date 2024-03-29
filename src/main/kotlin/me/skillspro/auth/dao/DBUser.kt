package me.skillspro.auth.dao

import me.skillspro.core.data.Audited
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class DBUser(var name: String,
             @field:Id var email: String,
             var password: String?,
             var emailVerified: Boolean,
             var photo: String?) : Audited(){

}