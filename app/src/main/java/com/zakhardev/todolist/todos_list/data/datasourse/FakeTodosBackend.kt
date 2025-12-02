package com.zakhardev.todolist.todos_list.data.datasourse

import com.zakhardev.todolist.todos_list.domain.model.TodoItem
import com.zakhardev.todolist.todos_list.domain.repository.TodosBackend
import org.slf4j.LoggerFactory

class FakeTodosBackend : TodosBackend {
    private val log = LoggerFactory.getLogger(FakeTodosBackend::class.java)

    override suspend fun getAll(): List<TodoItem> {
        log.info("backend.getAll called")
        return emptyList()
    }

    override suspend fun upsert(item: TodoItem) {
        log.info("backend.upsert uid={}", item.uid)
    }

    override suspend fun delete(uid: String) {
        log.info("backend.delete uid={}", uid)
    }
}
