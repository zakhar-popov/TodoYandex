package com.zakhardev.todolist.todos_edit.presentation

import android.graphics.Color.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zakhardev.todolist.todos_list.data.TodosRepository
import com.zakhardev.todolist.todos_list.domain.Importance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val repository: TodosRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditUiState())
    val state: StateFlow<EditUiState> = _state

    fun load(todoUid: String) {
        viewModelScope.launch {
            val existing = repository.getById(todoUid)

            if (existing != null) {
                _state.value = EditUiState.fromDomain(existing)
            } else {
                _state.value = EditUiState(loading = false)
            }
        }
    }

    fun save(onBack: () -> Unit) {
        viewModelScope.launch {
            val result = repository.upsert(_state.value.toDomain())
            onBack()
        }
    }

    fun onTextChange(value: TextFieldValue) = _state.update { it.copy(text = value) }
    fun onToggleDone(v: Boolean) = _state.update { it.copy(isDone = v) }
    fun onImportance(importance: Importance) = _state.update { it.copy(importance = importance) }
    fun onPickDateClick() = _state.update { it.copy(showDatePicker = true) }
    fun onDismissDate() = _state.update { it.copy(showDatePicker = false) }
    fun onConfirmDate(millis: Long?) =
        _state.update { it.copy(deadlineMillis = millis, showDatePicker = false) }

    fun onOpenPicker() = _state.update { it.copy(showColorPicker = true) }
    fun onDismissPicker() = _state.update { it.copy(showColorPicker = false) }
    fun onHue(value: Float) = _state.update { it.copy(customHue = value.coerceIn(0f, 360f)) }
    fun onSatVal(sat: Float, value: Float) =
        _state.update { it.copy(customSat = sat.coerceIn(0f, 1f), customVal = value.coerceIn(0f, 1f)) }

    fun onConfirmCustom() = _state.update { it.copy(selectedFromCustom = true, showColorPicker = false) }

    fun onSelectPresetColor(color: Color) = _state.update {
        val hsv = FloatArray(3)
        colorToHSV(color.toArgb(), hsv)
        it.copy(
            customHue = hsv[0],
            customSat = hsv[1],
            customVal = hsv[2],
            selectedFromCustom = false
        )
    }
}
