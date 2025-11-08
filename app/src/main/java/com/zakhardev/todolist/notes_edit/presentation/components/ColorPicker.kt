package com.zakhardev.todolist.notes_edit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    hue: Float,
    onHueChange: (Float) -> Unit,
    saturation: Float,
    value: Float,
    onSatValChange: (Float, Float) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color = Color.hsv(hue, saturation, value)

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color)
            )
            Text(text = "Яркость")
            Slider(
                value = value,
                onValueChange = { onSatValChange(saturation, it) },
                valueRange = 0f..1f
            )
        }
        HueSVPicker(
            hue = hue,
            onHueChange = onHueChange,
            saturation = saturation,
            value = value,
            onSatValChange = onSatValChange
        )
    }
}