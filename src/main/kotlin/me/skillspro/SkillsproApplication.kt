package me.skillspro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class SkillsproApplication

fun main(args: Array<String>) {
	runApplication<SkillsproApplication>(*args)
}
