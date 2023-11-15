package me.skillspro.core.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import redis.clients.jedis.JedisPool


@Configuration
class RedisConfig(private val configProperties: ConfigProperties) : DisposableBean {
    private val logger = LoggerFactory.getLogger(javaClass)

    private lateinit var jedisPool: JedisPool

    @Bean
    fun jedisPool(): JedisPool {
        this.jedisPool = JedisPool(configProperties.redis.host, configProperties.redis.port)
        logger.info("Redis connected... :")
        return this.jedisPool
    }

    @Throws(Exception::class)
    override fun destroy() {
        logger.info("Destroying jedis")
        this.jedisPool.close()
    }

}