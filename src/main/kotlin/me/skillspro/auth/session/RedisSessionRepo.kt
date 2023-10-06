package me.skillspro.auth.session

import me.skillspro.auth.session.data.SessionUser
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RedisSessionRepo : CrudRepository<SessionUser, String>{
}