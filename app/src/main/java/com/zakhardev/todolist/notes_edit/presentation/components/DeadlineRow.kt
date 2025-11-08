package com.zakhardev.todolist.notes_edit.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun DeadlineRow(
    deadlineMillis: Long?,
    onPick: () -> Unit,
    isDone: Boolean
) {
    val alpha = if (isDone) 0.4f else 1f

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Button(onClick = onPick) { Text("Выбрать дату") }

        Text(text = "Дедлайн: " + (deadlineMillis?.let {
            java.text.DateFormat.getDateTimeInstance().format(java.util.Date(it))
        } ?: "Не задан"))
    }
}
