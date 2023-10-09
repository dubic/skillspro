package me.skillspro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableMongoRepositories
@EnableAsync
class SkillsproApplication

fun main(args: Array<String>) {
	runApplication<SkillsproApplication>(*args)
}
