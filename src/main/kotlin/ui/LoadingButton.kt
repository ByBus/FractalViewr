package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingButton(
    percent: Float = 50f,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
) {
    val gradientPosition = percent / 100f
    val gradient = Brush.horizontalGradient(
        gradientPosition - 0.01f to Color.Transparent,
        gradientPosition to Color.LightGray,
    )
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
    ) {
        Box(
            modifier = modifier
                .background(
                    brush = gradient,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 16.dp)
                .height(ButtonDefaults.MinHeight),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "%.0f%%".format(percent), style = MaterialTheme.typography.subtitle1)
        }
    }
}

