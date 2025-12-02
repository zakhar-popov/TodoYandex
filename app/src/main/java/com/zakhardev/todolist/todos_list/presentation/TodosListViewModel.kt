package com.zakhardev.todolist.todos_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zakhardev.todolist.todos_list.data.repository.TodosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosListViewModel @Inject constructor(
    private val repository: TodosRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TodosUiState())
    val state: StateFlow<TodosUiState> = _state

    init {
        viewModelScope.launch {
            repository.itemsFlow.collect { list ->
                _state.update {
                    it.copy(
                        loading = false,
                        items = list,
                        error = null
                    )
                }
            }
        }
    }

    fun onDelete(uid: String) = viewModelScope.launch {
        repository.delete(uid)
        // _state.value = _state.value.copy(items = _state.value.items.filterNot { it.uid == uid })
    }
}