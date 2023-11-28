package me.skillspro.core.storage

import jakarta.servlet.http.HttpServletResponse
import me.skillspro.core.BaseController
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.RequestContextHolder
import java.io.IOException
import java.io.InputStream

@Controller
@RequestMapping("/content")
class StorageController(private val storageService: StorageService) : BaseController() {
    private val logger = LoggerFactory.getLogger(javaClass)
    @GetMapping("/{key}")
    fun view(@PathVariable("key") key: String, response: HttpServletResponse) {
        try {
            val inputStream = storageService.stream(key)
            IOUtils.copy(inputStream, response.outputStream)
            IOUtils.closeQuietly(inputStream)
        } catch (e: IOException) {
            logger.error(e.message, e)
        }
    }

    @GetMapping()
    fun getUrl(): ResponseEntity<Any>{

        val currentRequestAttributes = RequestContextHolder.currentRequestAttributes()

//        println("ATTR PATH 0:: ${currentRequestAttributes.getAttribute(
//                "org.springframework.web.util.ServletRequestPathUtils.PATH", 0)}")
        return ResponseEntity.ok(currentRequestAttributes.getAttributeNames(0).map {
            it to currentRequestAttributes.getAttribute(it, 0).toString()
        })

//        return ResponseEntity.ok(
//                mapOf(
//                        "attrPath0" to currentRequestAttributes.getAttribute(
//                                "org.springframework.web.util.ServletRequestPathUtils.PATH", 0),
//                        "attrPath0" to currentRequestAttributes.getAttribute(
//                                "org.springframework.web.util.ServletRequestPathUtils.PATH", 0)
//
//                        )
//               )

    }
}