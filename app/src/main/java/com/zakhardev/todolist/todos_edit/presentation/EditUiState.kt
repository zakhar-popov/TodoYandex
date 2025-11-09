package com.zakhardev.todolist.todos_edit.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import com.zakhardev.todolist.todos_list.domain.Importance
import com.zakhardev.todolist.todos_list.domain.TodoItem
import java.util.UUID

data class EditUiState(
    val uid: String = UUID.randomUUID().toString(),
    val text: TextFieldValue = TextFieldValue(""),
    val deadlineMillis: Long? = null,
    val isDone: Boolean = false,
    val importance: Importance = Importance.NORMAL,
    val customHue: Float = 120f,
    val customSat: Float = 0.6f,
    val customVal: Float = 0.9f,
    val selectedFromCustom: Boolean = false,
    val showDatePicker: Boolean = false,
    val showColorPicker: Boolean = false,
    val loading: Boolean = true
) {
    private val colorInt: Int get() = Color.hsv(customHue, customSat, customVal).toArgb()

    fun toDomain(): TodoItem = TodoItem(
        uid = uid,
        text = text.text,
        importance = importance,
        color = colorInt,
        deadline = deadlineMillis?.let { java.time.Instant.ofEpochMilli(it) },
        isDone = isDone
    )

    companion object {
        fun fromDomain(item: TodoItem): EditUiState = EditUiState(
            uid = item.uid,
            text = TextFieldValue(item.text),
            deadlineMillis = item.deadline?.toEpochMilli(),
            isDone = item.isDone,
            importance = item.importance,
            customHue = 120f,
            customSat = 0.0f,
            customVal = 1.0f,
            selectedFromCustom = false,
            loading = false
        )
    }
}