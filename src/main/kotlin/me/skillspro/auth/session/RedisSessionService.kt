package me.skillspro.auth.session

import me.skillspro.auth.models.Email
import me.skillspro.auth.models.Name
import me.skillspro.auth.models.User
import me.skillspro.auth.session.data.SessionUser
import me.skillspro.core.config.ConfigProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RedisSessionService(
        private val repo: RedisSessionRepo,
        private val configProperties: ConfigProperties) :
        SessionService {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun createSession(token: String, user: User) {
        repo.save(SessionUser(token, user.name.value, user.email.value,
                user.isVerified(),
                configProperties.redisSessionTtlSecs))
        logger.info("Session created for [${user.email.value}]")
    }

    override fun userInSession(token: String): User? {
        return try {
            val s = repo.findById(token).get()
            User(Name(s.name), Email(s.email, s.verified))
        } catch (ex: NoSuchElementException) {
            logger.warn("Session not found with token [...]")
            null
        }
    }
}