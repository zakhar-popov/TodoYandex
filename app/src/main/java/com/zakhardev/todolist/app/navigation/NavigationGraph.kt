package com.zakhardev.todolist.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.scene.rememberSceneSetupNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.zakhardev.todolist.todos_edit.presentation.EditTodoScreen
import com.zakhardev.todolist.todos_list.presentation.TodosListScreen

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier
) {
    val backStack = remember { mutableStateListOf<Any>(TodosList) }

    NavDisplay(
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is TodosList -> NavEntry(key) {
                    TodosListScreen(
                        onTodoClick = { todoUid ->
                            backStack.add(TodoEdit(todoUid))
                        },
                        onCreateTodo = {
                            backStack.add(TodoCreate)
                        },
                        modifier = modifier
                    )
                }

                is TodoCreate -> NavEntry(key) {
                    EditTodoScreen(
                        todoUid = null,
                        onBack = { backStack.removeLastOrNull() },
                        modifier = modifier
                    )
                }

                is TodoEdit -> NavEntry(key) {
                    EditTodoScreen(
                        todoUid = key.uid,
                        onBack = { backStack.removeLastOrNull() },
                        modifier = modifier
                    )
                }

                else -> NavEntry(Unit) { Text("Неверный путь") }
            }
        }
    )
}

