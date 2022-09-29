package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextGradientButton(
    text: String,
    gradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(4.dp)
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.body1.copy(shadow = Shadow(
            color = Color.Black,
            blurRadius = 1f
        )),
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = modifier
            .padding(vertical = 4.dp)
            .border(1.dp, MaterialTheme.colors.secondaryVariant, shape)
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .clip(shape)
            .background(brush = Brush.horizontalGradient(gradient))
            .padding(4.dp)
    )
}

@Composable
@Preview
fun TextGradientButtonPreview(){
    TextGradientButton(
        "Gradient 1",
        listOf(Color.Red, Color.Green, Color.Blue),
        {}
    )
}