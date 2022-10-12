package ui.style

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import ui.style.fonts.FractalThemeTypography

@Composable
fun FractalTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content,
        typography = FractalThemeTypography
    )
}