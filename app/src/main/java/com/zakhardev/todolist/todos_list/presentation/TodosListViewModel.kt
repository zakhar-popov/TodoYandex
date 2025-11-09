package com.zakhardev.todolist.todos_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zakhardev.todolist.todos_list.data.TodosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosListViewModel @Inject constructor(
    private val repository: TodosRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TodosUiState())
    val state: StateFlow<TodosUiState> = _state

    fun onDelete(uid: String) = viewModelScope.launch {
        repository.delete(uid)
        loadTodos()
       // _state.value = _state.value.copy(items = _state.value.items.filterNot { it.uid == uid })
    }

    fun loadTodos() = viewModelScope.launch {
        _state.value = _state.value.copy(loading = true)

        try {
            val todos = repository.getAll()
            _state.value = _state.value.copy(items = todos, loading = false, error = null)

        } catch (e: Exception) {
            _state.value = _state.value.copy(loading = false, error = e.localizedMessage)
        }
    }
}