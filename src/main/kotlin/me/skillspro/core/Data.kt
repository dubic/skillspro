package me.skillspro.core

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

open class Data {
    fun toJson(): String {
        val mapper = ObjectMapper()
        return try {
            mapper.writeValueAsString(this)
        } catch (ex: JsonProcessingException) {
            ex.printStackTrace()
            ex.message ?: ""
        }
    }
}