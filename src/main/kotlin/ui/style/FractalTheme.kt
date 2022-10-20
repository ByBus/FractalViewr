package ui.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import ui.style.fonts.FractalThemeTypography

private val LightColors = lightColors(
    onError = md_theme_light_onError,
    error = md_theme_light_error,
    onBackground = md_theme_light_onBackground,
    background = md_theme_light_background,
    onSurface = md_theme_light_onSurface,
    surface = md_theme_light_surface,
    onSecondary = md_theme_light_onSecondary,
    secondary = md_theme_light_secondary,
    onPrimary = md_theme_light_onPrimary,
    primary = md_theme_light_primary,
)


private val DarkColors = darkColors(
    onError = md_theme_dark_onError,
    error = md_theme_dark_error,
    onBackground = md_theme_dark_onBackground,
    background = md_theme_dark_background,
    onSurface = md_theme_dark_onSurface,
    surface = md_theme_dark_surface,
    onSecondary = md_theme_dark_onSecondary,
    secondary = md_theme_dark_secondary,
    onPrimary = md_theme_dark_onPrimary,
    primary = md_theme_dark_primary,
)
@Composable
fun FractalTheme(useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        content = content,
        typography = FractalThemeTypography,
        colors = colors
    )
}