package ru.user.todolist.domain

import java.io.Serializable
import java.time.Instant
import java.util.*

data class Item(
    var id: Int? = null,
    var text: String,
    var lastModifyDate: Date = Date.from(Instant.now())
) : Serializable