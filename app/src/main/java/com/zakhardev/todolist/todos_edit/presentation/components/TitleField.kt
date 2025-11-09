package com.zakhardev.todolist.todos_edit.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TitleField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isDone: Boolean
) {
    val minHeight = 120.dp
    val alpha = if (isDone) 0.4f else 1f

    OutlinedTextField(
        label = {
            Text(text = "Дело о: ")
        },
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .wrapContentHeight()
            .alpha(alpha),
        minLines = 4,
        maxLines = Int.MAX_VALUE
    )
}