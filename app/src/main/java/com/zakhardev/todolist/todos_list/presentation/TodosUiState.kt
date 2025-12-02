package com.zakhardev.todolist.todos_list.presentation

import com.zakhardev.todolist.todos_list.domain.model.TodoItem

data class TodosUiState(
    val items: List<TodoItem> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)