package com.zakhardev.todolist.todos_list.domain

import java.time.Instant
import java.util.UUID
import android.graphics.Color

data class TodoItem(
    val uid: String = UUID.randomUUID().toString(),
    val text: String,
    val importance: Importance = Importance.NORMAL,
    val color: Int = Color.WHITE,
    val deadline: Instant? = null,
    val isDone: Boolean = false
)