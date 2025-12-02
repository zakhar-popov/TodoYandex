package com.zakhardev.todolist.todos_list.domain.repository

import com.zakhardev.todolist.todos_list.domain.model.TodoItem

interface TodosBackend {
    suspend fun getAll(): List<TodoItem>
    suspend fun upsert(item: TodoItem)
    suspend fun delete(uid: String)
}
