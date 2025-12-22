package com.zakhardev.todolist.todos_list.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey val uid: String,
    val text: String,
    val importance: String,
    val color: Int,
    val deadlineEpochMs: Long?,
    val isDone: Boolean,

   // val createdAtEpochMs: Long
)
