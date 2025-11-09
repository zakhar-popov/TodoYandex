package com.zakhardev.todolist.todos_list.data

import com.zakhardev.todolist.todos_list.domain.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodosRepository(
    private val storage: FileStorage
) {

    suspend fun getAll(): List<TodoItem> = withContext(Dispatchers.IO) { storage.items }

    suspend fun getById(uid: String): TodoItem? = withContext(Dispatchers.IO) {
        storage.items.firstOrNull { it.uid == uid }
    }

    suspend fun upsert(item: TodoItem): Boolean = withContext(Dispatchers.IO) {
        storage.add(item); storage.save()
    }

    suspend fun delete(uid: String): Boolean = withContext(Dispatchers.IO) {
        val ok = storage.remove(uid); storage.save(); ok
    }
}