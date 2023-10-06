package me.skillspro.core

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration


@Configuration
class MongoConfig(private val configProperties: ConfigProperties) : AbstractMongoClientConfiguration() {
    override fun getDatabaseName(): String {
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