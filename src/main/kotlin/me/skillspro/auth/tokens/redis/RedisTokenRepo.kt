package me.skillspro.auth.tokens.redis

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.QueryByExampleExecutor
import org.springframework.stereotype.Repository

@Repository
interface RedisTokenRepo: CrudRepository<TokenHash, String>, QueryByExampleExecutor<TokenHash> {
//    fun findByUserAndType(user: String, type: String): List<TokenHash>
}