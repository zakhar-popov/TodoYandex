package com.zakhardev.todolist.notes_edit.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.zakhardev.todolist.notes_list.domain.Importance

data class EditUiState(
    val text: TextFieldValue = TextFieldValue(""),
    val deadlineMillis: Long? = null,
    val isDone: Boolean = false,
    val importance: Importance = Importance.NORMAL,
    val customHue: Float = 120f,
    val customSat: Float = 0.6f,
    val customVal: Float = 0.9f,
    val selectedFromCustom: Boolean = false,
    val showDatePicker: Boolean = false,
    val showColorPicker: Boolean = false
) {
    val selectedColor: Color get() = if (selectedFromCustom) Color.hsv(customHue, customSat, customVal) else Color(0xFFC8E6C9)
}