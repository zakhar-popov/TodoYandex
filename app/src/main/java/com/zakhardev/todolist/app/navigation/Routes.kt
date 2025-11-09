package com.zakhardev.todolist.app.navigation

import kotlinx.serialization.Serializable

@Serializable
data object TodosList

@Serializable
data class TodoEdit(val uid: String)

@Serializable
data object TodoCreate