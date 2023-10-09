package me.skillspro.core.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate


@Configuration
class RedisConfig(private val configProperties: ConfigProperties) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        return LettuceConnectionFactory(RedisStandaloneConfiguration(configProperties.redis.host, configProperties.redis.port))
    }

    @Bean
    fun stringRedisTemplate(redisConnectionFactory: RedisConnectionFactory?): StringRedisTemplate {
        logger.info("REDIS CONNECTION:: ${redisConnectionFactory?.connection}")
        val template = StringRedisTemplate()
        template.connectionFactory = redisConnectionFactory
        return template
    }


}