package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TextGradientButton(
    text: String,
    gradient: List<Pair<Float, Color>>,
    onClick: () -> Unit,
    cornerRadius: Dp = 4.dp,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(cornerRadius)
    val horPadding = 32.dp
    val verPadding = 8.dp
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.body1.copy(
            shadow = Shadow(
                color = Color.Black,
                blurRadius = 1f
            )
        ),
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, MaterialTheme.colors.secondaryVariant, shape)
            .clickable(onClick = onClick)
            .clip(shape)
            .background(brush = Brush.horizontalGradient(*gradient.toTypedArray()))
            .padding(vertical = verPadding, horizontal = horPadding)
            .drawBehind {
                //top rim glare
                val top = -verPadding.value + 1f
                drawLine(
                    color = Color.LightGray,
                    start = Offset(-horPadding.value, top),
                    end = Offset(size.width + horPadding.value, top),
                    strokeWidth = 2f
                )
            }
    )

}