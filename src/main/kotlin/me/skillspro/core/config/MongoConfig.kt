package me.skillspro.core.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration


@Configuration
class MongoConfig(private val configProperties: ConfigProperties,
                  private val environment: Environment) :
        AbstractMongoClientConfiguration() {
    override fun getDatabaseName(): String {
        if (this.environment.acceptsProfiles (Profiles.of("e2e")) ){
            return "${configProperties.mongo.dbName}_e2e"
        }
        return configProperties.mongo.dbName
    }

    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(configProperties.mongo.url)
        val mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build()
        return MongoClients.create(mongoClientSettings)
    }
}