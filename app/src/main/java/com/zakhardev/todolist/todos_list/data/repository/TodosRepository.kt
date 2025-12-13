package com.zakhardev.todolist.todos_list.data.repository

import com.zakhardev.todolist.todos_list.data.datasourse.FileStorage
import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import com.zakhardev.todolist.todos_list.domain.repository.TodosBackend
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
    private val storage: FileStorage, // local
    private val backend: TodosBackend // remote
) {
    private val _itemsFlow = MutableStateFlow<List<TodoItem>>(emptyList())
    val itemsFlow: StateFlow<List<TodoItem>> = _itemsFlow

    val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            loadFromCache()
            synchronize()
        }
    }

    private suspend fun loadFromCache() = withContext(Dispatchers.IO) {
        storage.load()
        _itemsFlow.value = storage.items
    }

    suspend fun synchronize(): Boolean = withContext(Dispatchers.IO) {
        val okLoad = storage.load()
        if (!okLoad) return@withContext false

        val local = storage.items

        val serverList = runCatching {
            if (local.isEmpty()) {
                backend.getAll()
            } else {
                backend.patchAll(local)
            }
        }.getOrElse { return@withContext false }

        // верим серверу
        storage.replaceAll(serverList)

        val okSave = storage.save()
        if (okSave) _itemsFlow.value = storage.items
        okSave
    }

    fun getByIdFlow(uid: String): Flow<TodoItem?> =
        itemsFlow.map { list -> list.firstOrNull { it.uid == uid } }

    suspend fun upsert(item: TodoItem): Boolean = withContext(Dispatchers.IO) {
        storage.load()
        storage.add(item)

        val ok = storage.save()
        if (ok) {
            _itemsFlow.value = storage.items
            backend.upsert(item)
        }
        ok
    }

    suspend fun delete(uid: String): Boolean = withContext(Dispatchers.IO) {
        storage.load()

        val ok = storage.remove(uid)
        if (ok) {
            storage.save()
            _itemsFlow.value = storage.items
            backend.delete(uid)
        }
        ok
    }
}