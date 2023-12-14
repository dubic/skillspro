package me.skillspro.core.data

import java.time.LocalDateTime
import java.time.LocalDateTime.now

open class Audited(var createDate: LocalDateTime = now(), var updatedDate: LocalDateTime = now()) {
}