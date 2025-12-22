package com.zakhardev.todolist.todos_list.data.datasourse

import com.zakhardev.todolist.todos_list.data.room.TodosDao
import com.zakhardev.todolist.todos_list.data.room.toDomain
import com.zakhardev.todolist.todos_list.data.room.toEntity
import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import com.zakhardev.todolist.todos_list.domain.repository.TodosLocalCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class RoomTodosLocalCache(
    private val dao: TodosDao
) : TodosLocalCache {

    override fun observeAll(): Flow<List<TodoItem>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(uid: String): Flow<TodoItem?> =
        dao.observeById(uid).map { it?.toDomain() }

    override suspend fun getAllOnce(): List<TodoItem> =
        dao.getAllOnce().map { it.toDomain() }

    override suspend fun upsert(item: TodoItem) {
        dao.upsert(item.toEntity())
    }

    override suspend fun delete(uid: String) {
        dao.delete(uid)
    }

    override suspend fun replaceAll(items: List<TodoItem>) {
        dao.clear()
        dao.insertAll(items.map { it.toEntity() })
    }

    override suspend fun clear() {
        dao.clear()
    }
}
