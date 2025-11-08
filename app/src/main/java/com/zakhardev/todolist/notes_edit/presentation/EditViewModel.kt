package com.zakhardev.todolist.notes_edit.presentation

import android.graphics.Color.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.zakhardev.todolist.notes_list.domain.Importance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EditViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val KEY = "EditUiState"
    private val _state = MutableStateFlow(savedStateHandle.get<EditUiState>(KEY) ?: EditUiState())
    val state: StateFlow<EditUiState> = _state

    fun onTextChange(v: TextFieldValue) = _state.update { it.copy(text = v) }
    fun onToggleDone(v: Boolean) = _state.update { it.copy(isDone = v) }
    fun onImportance(v: Importance) = _state.update { it.copy(importance = v) }

    fun onPickDateClick() = _state.update { it.copy(showDatePicker = true) }
    fun onDismissDate() = _state.update { it.copy(showDatePicker = false) }
    fun onConfirmDate(millis: Long?) = _state.update { it.copy(deadlineMillis = millis, showDatePicker = false) }

    fun onSelectPresetColor(color: Color) = _state.update {
        val hsv = FloatArray(3)
        colorToHSV(color.toArgb(), hsv)
        it.copy(
            customHue = hsv[0].coerceIn(0f, 360f),
            customSat = hsv[1].coerceIn(0f, 1f),
            customVal = hsv[2].coerceIn(0f, 1f),
            selectedFromCustom = false
        )
    }

    fun onOpenPicker() = _state.update { it.copy(showColorPicker = true) }
    fun onDismissPicker() = _state.update { it.copy(showColorPicker = false) }
    fun onHue(v: Float) = _state.update { it.copy(customHue = v.coerceIn(0f, 360f)) }
    fun onSatVal(s: Float, v: Float) = _state.update { it.copy(customSat = s.coerceIn(0f, 1f), customVal = v.coerceIn(0f, 1f)) }
    fun onConfirmCustom() = _state.update { it.copy(selectedFromCustom = true, showColorPicker = false) }

    override fun onCleared() {
        savedStateHandle[KEY] = _state.value
        super.onCleared()
    }
}