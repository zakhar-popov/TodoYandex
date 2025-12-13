package com.zakhardev.todolist.todos_list.data.model

import android.graphics.Color
import com.zakhardev.todolist.todos_list.domain.model.Importance
import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import java.time.Instant

private fun Importance.toApi(): String = when (this) {
    Importance.LOW -> "low"
    Importance.NORMAL -> "basic"
    Importance.HIGH -> "important"
}

private fun String.toDomainImportance(): Importance = when (this.lowercase()) {
    "low" -> Importance.LOW
    "basic" -> Importance.NORMAL
    "important" -> Importance.HIGH
    else -> Importance.NORMAL
}

private fun Int.toHexOrNull(): String? {
    if (this == Color.WHITE) return null
    return String.format("#%06X", 0xFFFFFF and this)
}

private fun String?.toColorIntOrWhite(): Int {
    if (this.isNullOrBlank()) return Color.WHITE
    return try {
        Color.parseColor(this)
    } catch (_: IllegalArgumentException) {
        Color.WHITE
    }
}

fun TodoItemDto.toDomain(): TodoItem = TodoItem(
    uid = id,
    text = text,
    importance = importance.toDomainImportance(),
    color = color.toColorIntOrWhite(),
    deadline = deadline?.let { Instant.ofEpochMilli(it) },
    isDone = done
)

fun TodoItem.toDto(
    deviceId: String = "default_id",
    createdAtMillis: Long = System.currentTimeMillis(),
    changedAtMillis: Long = System.currentTimeMillis(),
): TodoItemDto = TodoItemDto(
    id = uid,
    text = text,
    importance = importance.toApi(),
    deadline = deadline?.toEpochMilli(),
    done = isDone,
    color = color.toHexOrNull(),
    createdAt = createdAtMillis,
    changedAt = changedAtMillis,
    lastUpdatedBy = deviceId
)
