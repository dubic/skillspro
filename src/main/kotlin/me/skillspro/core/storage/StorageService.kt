package me.skillspro.core.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.file.Path


interface StorageService {
    fun store(file: MultipartFile, key: String?): String

    fun load(filename: String): Path?

    fun loadAsResource(filename: String): Resource?

    fun delete(filename: String?)
    fun stream(filename: String): InputStream
}