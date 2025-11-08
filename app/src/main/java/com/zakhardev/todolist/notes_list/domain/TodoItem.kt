package com.zakhardev.todolist.notes_list.domain

import java.time.Instant
import java.util.UUID
import android.graphics.Color
import com.zakhardev.todolist.notes_list.domain.Importance

data class TodoItem(
    val uid: UUID = UUID.randomUUID(),
    val text: String,
    val importance: Importance = Importance.NORMAL,
    val color: Int = Color.WHITE,
    val deadline: Instant? = null,
    val isDone: Boolean = false
)