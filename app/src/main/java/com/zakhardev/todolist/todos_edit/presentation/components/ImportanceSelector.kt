package com.zakhardev.todolist.todos_edit.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.zakhardev.todolist.todos_list.domain.model.Importance

@Composable
fun ImportanceSelector(
    importance: Importance,
    onChange: (Importance) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = importance == Importance.LOW,
                onClick = { onChange(Importance.LOW) },
                label = { Text("😴 Неважно") }
            )
        }
        item {
            FilterChip(
                selected = importance == Importance.NORMAL,
                onClick = { onChange(Importance.NORMAL) },
                label = { Text("🙏 Обычно") }
            )
        }
        item {
            FilterChip(
                selected = importance == Importance.HIGH,
                onClick = { onChange(Importance.HIGH) },
                label = { Text("❗Сверхважно") }
            )
        }
    }
}

