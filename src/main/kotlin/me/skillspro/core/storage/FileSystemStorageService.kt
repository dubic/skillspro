package me.skillspro.core.storage

import me.skillspro.core.config.ConfigProperties
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import java.util.*

@Service
@ConditionalOnProperty(prefix = "sp", name = ["storage.service"], havingValue = "file")
class FileSystemStorageService(private val configProperties: ConfigProperties) : StorageService {
    private val logger = LoggerFactory.getLogger(javaClass)

    val defaultDirectory: String = System.getProperty("user.home")
    override fun store(file: MultipartFile, key: String?): String {
        val nameExtension = key?.plus("-")?.plus(UUID.randomUUID().toString())?.plus(extension
        (file))
                ?: UUID.randomUUID().toString().plus(extension(file))

        val fileNameExtension = "${file.name}-$nameExtension"
        saveNewFile(fileNameExtension, file.inputStream)
        return buildUrl(fileNameExtension)
    }

    private fun extension(file: MultipartFile): String {
        logger.debug("Content type :: {}", file.contentType)
        logger.debug("Original File name :: {}", file.originalFilename)
        return ".".plus(file.originalFilename?.substringAfterLast(".")?.lowercase()
                ?: file.contentType?.substringAfterLast("/"))
    }

    private fun buildUrl(name: String): String {
        return "${configProperties.applicationUrl}/content/$name"
    }

    private fun saveNewFile(name: String, inputStream: InputStream): File {
        val newfile: File = toFile(name)
        val outputStream: OutputStream = FileUtils.openOutputStream(newfile)
        IOUtils.copyLarge(inputStream, outputStream)
        IOUtils.closeQuietly(inputStream)
        IOUtils.closeQuietly(outputStream)
        logger.debug("saved file:: {}", newfile.absolutePath)
        return newfile
    }

    override fun load(filename: String): Path? {
        TODO("Not yet implemented")
    }

    override fun loadAsResource(filename: String): Resource? {
        TODO("Not yet implemented")
    }

    override fun delete(filename: String?) {
        logger.debug("about to delete file :: $filename")
        if (filename == null) {
            return
        }
        FileUtils.deleteQuietly(toFile(filename))
        logger.debug("deleted file :: $filename")
    }

    override fun stream(filename: String): InputStream {
        return FileUtils.openInputStream(toFile(filename))
    }

    private fun toFile(filename: String) = File(defaultDirectory + File.separator + filename)
}