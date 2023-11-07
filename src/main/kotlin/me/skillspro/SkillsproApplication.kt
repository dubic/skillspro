package me.skillspro

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableAsync


@SpringBootApplication
@EnableMongoRepositories
@EnableAsync
class SkillsproApplication

fun main(args: Array<String>) {
	firebase()
	runApplication<SkillsproApplication>(*args)
}

fun firebase(){
	val options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.getApplicationDefault())
//			.setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
			.build()

	val app = FirebaseApp.initializeApp(options)
	println("Firebase app ::: ${app.name}")
	println("Firebase app project ::: ${app.options.projectId}")
}
