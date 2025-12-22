package com.zakhardev.todolist.todos_list.domain.repository

import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodosLocalCache {
    fun observeAll(): Flow<List<TodoItem>>
    fun observeById(uid: String): Flow<TodoItem?>

    suspend fun getAllOnce(): List<TodoItem>
    suspend fun upsert(item: TodoItem)
    suspend fun delete(uid: String)

    suspend fun replaceAll(items: List<TodoItem>)
    suspend fun clear()
}