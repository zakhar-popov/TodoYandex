package com.zakhardev.todolist.todos_list.data.room

import com.zakhardev.todolist.todos_list.domain.model.Importance
import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import java.time.Instant

fun TodoEntity.toDomain(): TodoItem =
    TodoItem(
        uid = uid,
        text = text,
        importance = Importance.valueOf(importance),
        color = color,
        deadline = deadlineEpochMs?.let { Instant.ofEpochMilli(it) },
        isDone = isDone
    )

fun TodoItem.toEntity(nowEpochMs: Long = System.currentTimeMillis()): TodoEntity =
    TodoEntity(
        uid = uid,
        text = text,
        importance = importance.name,
        color = color,
        deadlineEpochMs = deadline?.toEpochMilli(),
        isDone = isDone,
        createdAtEpochMs = nowEpochMs
    )
