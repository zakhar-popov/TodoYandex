package com.zakhardev.todolist.todos_list.data.datasourse

import com.zakhardev.todolist.todos_list.domain.repository.TodosLocalCache
import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class FileTodosLocalCache(
    private val storage: FileStorage
) : TodosLocalCache {

    private val state = MutableStateFlow<List<TodoItem>>(emptyList())

    init {
        storage.load()
        state.value = storage.items
    }

    override fun observeAll(): Flow<List<TodoItem>> = state

    override fun observeById(uid: String): Flow<TodoItem?> =
        state.map { list -> list.firstOrNull { it.uid == uid } }

    override suspend fun getAllOnce(): List<TodoItem> = withContext(Dispatchers.IO) {
        storage.load()
        storage.items
    }

    override suspend fun upsert(item: TodoItem) = withContext(Dispatchers.IO) {
        storage.load()
        storage.add(item)
        storage.save()
        state.value = storage.items
    }

    override suspend fun delete(uid: String) = withContext(Dispatchers.IO) {
        storage.load()
        storage.remove(uid)
        storage.save()
        state.value = storage.items
    }

    override suspend fun replaceAll(items: List<TodoItem>) = withContext(Dispatchers.IO) {
        storage.replaceAll(items)
        storage.save()
        state.value = storage.items
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        storage.replaceAll(emptyList())
        storage.save()
        state.value = emptyList()
    }
}
