package ui.gradientmaker

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun DrawScope.drawMarker(
    position: Offset,
    color: Color,
    radius: Dp = 15.dp,
    strokeWidth: Dp = 2.dp,
    drawOuterStroke: Boolean = true,
) {
    drawCircle(
        center = position,
        color = color,
        radius = radius.value
    )
    if (drawOuterStroke) {
        drawCircle(
            center = position,
            color = Color.LightGray,
            radius = radius.value + 1,
            style = Stroke(strokeWidth.value),
        )
    }
    drawCircle(
        center = position,
        color = Color.White,
        radius = radius.value,
        style = Stroke(strokeWidth.value),
    )
}