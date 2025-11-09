package com.zakhardev.todolist.todos_edit.presentation.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp

@Composable
fun HueSVPicker(
    hue: Float,
    onHueChange: (Float) -> Unit,
    saturation: Float,
    value: Float,
    onSatValChange: (Float, Float) -> Unit
) {
    val size = 220.dp
    var w by remember { mutableFloatStateOf(0f) }
    var h by remember { mutableFloatStateOf(0f) }

    val rainbow = listOf(
        Color.hsv(0f,   1f, value),
        Color.hsv(60f,  1f, value),
        Color.hsv(120f, 1f, value),
        Color.hsv(180f, 1f, value),
        Color.hsv(240f, 1f, value),
        Color.hsv(300f, 1f, value),
        Color.hsv(360f, 1f, value)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .drawWithContent {
                    drawRect(brush = Brush.horizontalGradient(rainbow))
                    drawRect(brush = Brush.verticalGradient(listOf(Color.Transparent, Color.White)))
                    val cx = (hue / 360f).coerceIn(0f, 1f) * this.size.width
                    val cy = (1f - saturation.coerceIn(0f, 1f)) * this.size.height
                    val cross = 9.dp.toPx()
                    val stroke = 2.dp.toPx()
                    drawLine(Color.Black, Offset(cx - cross, cy - cross), Offset(cx + cross, cy + cross), strokeWidth = stroke)
                    drawLine(Color.Black, Offset(cx - cross, cy + cross), Offset(cx + cross, cy - cross), strokeWidth = stroke)
                }
                .pointerInput(value) {
                    detectDragGestures { change, _ ->
                        if (w == 0f || h == 0f) return@detectDragGestures
                        val x = change.position.x.coerceIn(0f, w)
                        val y = change.position.y.coerceIn(0f, h)
                        val hNorm = (x / w).coerceIn(0f, 1f)
                        val sNorm = (1f - y / h).coerceIn(0f, 1f)
                        onHueChange(hNorm * 360f)
                        onSatValChange(sNorm, value)
                    }
                }
                .onGloballyPositioned {
                    w = it.size.width.toFloat()
                    h = it.size.height.toFloat()
                }
        )
    }
}
