package com.zakhardev.todolist.todos_list.data.model

@kotlinx.serialization.Serializable
data class TodoItemDto(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long? = null,
    @kotlinx.serialization.SerialName("done") val done: Boolean,
    val color: String? = null,
    @kotlinx.serialization.SerialName("created_at") val createdAt: Long,
    @kotlinx.serialization.SerialName("changed_at") val changedAt: Long,
    @kotlinx.serialization.SerialName("last_updated_by") val lastUpdatedBy: String,
)

@kotlinx.serialization.Serializable
data class FetchListResponse(val status: String, val list: List<TodoItemDto>, val revision: Int)

@kotlinx.serialization.Serializable
data class FetchItemResponse(val status: String, val element: TodoItemDto, val revision: Int)

@kotlinx.serialization.Serializable
data class ItemResponse(val status: String, val element: TodoItemDto, val revision: Int)

@kotlinx.serialization.Serializable
data class ElementRequest(val element: TodoItemDto)

@kotlinx.serialization.Serializable
data class PatchListRequest(val list: List<TodoItemDto>)
