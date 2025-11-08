package com.zakhardev.todolist.notes_edit.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zakhardev.todolist.notes_edit.presentation.components.ColorPicker
import com.zakhardev.todolist.notes_edit.presentation.components.DeadlineRow
import com.zakhardev.todolist.notes_edit.presentation.components.ImportanceSelector
import com.zakhardev.todolist.notes_edit.presentation.components.TitleField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoScreen(
    editViewModel: EditViewModel = viewModel(),
    modifier: Modifier
) {
    val state by editViewModel.state.collectAsStateWithLifecycle()
    val presets = listOf(
        Color(0xFFFFCDD2),
        Color(0xFFF8BBD0),
        Color(0xFFC8E6C9),
        Color(0xFFB2DFDB),
        Color(0xFFBBDEFB)
    )

    Column(
        modifier = modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TitleField(
            value = state.text,
            onValueChange = editViewModel::onTextChange,
            isDone = state.isDone
        )

        DeadlineRow(
            deadlineMillis = state.deadlineMillis,
            onPick = editViewModel::onPickDateClick,
            isDone = state.isDone
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(checked = state.isDone, onCheckedChange = editViewModel::onToggleDone)
            Text("Дело сделано")
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(presets.size) { i ->
                val currentColor = presets[i]
                val isSelected = !state.selectedFromCustom && currentColor.toArgb() == Color.hsv(
                    state.customHue,
                    state.customSat,
                    state.customVal
                ).toArgb()

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(currentColor)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) Color.Black else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { editViewModel.onSelectPresetColor(currentColor) }
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Red,
                                    Color.Yellow,
                                    Color.Green,
                                    Color.Cyan,
                                    Color.Blue,
                                    Color.Magenta
                                )
                            )
                        )
                        .border(
                            width = 2.dp,
                            color = if (state.selectedFromCustom) Color.Black else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .combinedClickable(
                            onClick = { editViewModel.onConfirmCustom() },
                            onLongClick = editViewModel::onOpenPicker
                        )
                )
            }
        }

        ImportanceSelector(importance = state.importance, onChange = editViewModel::onImportance)

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(state.selectedColor)
        )
    }

    if (state.showDatePicker) {
        val dateState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = editViewModel::onDismissDate,
            confirmButton = {
                TextButton(onClick = { editViewModel.onConfirmDate(dateState.selectedDateMillis) }) {
                    Text(
                        "OK"
                    )
                }
            },
            dismissButton = { TextButton(onClick = editViewModel::onDismissDate) { Text("Cancel") } }
        ) {
            DatePicker(state = dateState)
        }
    }

    if (state.showColorPicker) {
        AlertDialog(
            onDismissRequest = editViewModel::onDismissPicker,
            confirmButton = { TextButton(onClick = editViewModel::onConfirmCustom) { Text("Done") } },
            dismissButton = { TextButton(onClick = editViewModel::onDismissPicker) { Text("Cancel") } },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Выбор цвета")
                }
            },
            text = {
                ColorPicker(
                    hue = state.customHue,
                    onHueChange = editViewModel::onHue,
                    saturation = state.customSat,
                    value = state.customVal,
                    onSatValChange = editViewModel::onSatVal
                )
            }
        )
    }
}
