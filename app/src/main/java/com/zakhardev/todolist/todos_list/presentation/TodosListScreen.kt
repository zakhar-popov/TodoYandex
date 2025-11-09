package com.zakhardev.todolist.todos_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zakhardev.todolist.todos_list.presentation.components.SwipeToDeleteItem
import com.zakhardev.todolist.todos_list.presentation.components.TodoRow

@Composable
fun TodosListScreen(
    onTodoClick: (String) -> Unit,
    onCreateTodo: () -> Unit,
    modifier: Modifier = Modifier,
    listViewModel: TodosListViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        listViewModel.loadTodos()
    }
    val state by listViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onCreateTodo()
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddings ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddings),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = state.items,
                key = { it.uid }
            ) { item ->
                SwipeToDeleteItem(
                    onDelete = { listViewModel.onDelete(item.uid) },
                    content = {
                        TodoRow(
                            item = item,
                            onClick = { onTodoClick(item.uid) }
                        )
                    }
                )
            }
        }
    }
}

