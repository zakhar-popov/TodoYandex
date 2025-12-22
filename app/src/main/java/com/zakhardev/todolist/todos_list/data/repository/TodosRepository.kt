package com.zakhardev.todolist.todos_list.data.repository

import com.zakhardev.todolist.todos_list.data.datasourse.FileStorage
import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import com.zakhardev.todolist.todos_list.domain.repository.TodosBackend
import com.zakhardev.todolist.todos_list.domain.repository.TodosLocalCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext

class TodosRepository(
    private val localCache: TodosLocalCache, // local
    private val backend: TodosBackend // remote
) {
    val itemsFlow: Flow<List<TodoItem>> = localCache.observeAll()

    val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            synchronize()
        }
    }

    suspend fun synchronize(): Boolean {
        val local = localCache.getAllOnce()
        val serverList = runCatching {
            if (local.isEmpty()) backend.getAll() else backend.patchAll(local)
        }.getOrElse { return false }

        localCache.replaceAll(serverList)
        return true
    }

    fun getByIdFlow(uid: String): Flow<TodoItem?> = localCache.observeById(uid)

    suspend fun upsert(item: TodoItem): Boolean {
        localCache.upsert(item)
        backend.upsert(item)
        return true
    }

    suspend fun delete(uid: String): Boolean {
        localCache.delete(uid)
        backend.delete(uid)
        return true
    }
}