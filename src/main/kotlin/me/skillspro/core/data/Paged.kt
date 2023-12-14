package me.skillspro.core.data

import org.springframework.data.domain.Page

data class Paged<T>(val content: MutableList<T>,
               val totalElements: Long,
               val first: Boolean,
               val last: Boolean) {
    companion object {
        fun <T> fromPage(p: Page<T>): Paged<T> {
            return Paged(p.content, p.totalElements, p.isFirst, p.isLast)
        }
    }

}