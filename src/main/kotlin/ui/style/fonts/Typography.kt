package ui.style.fonts

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

private val Oswald = FontFamily(
    Font("font/oswald/Oswald-ExtraLight.ttf", FontWeight.W200),
    Font("font/oswald/Oswald-Light.ttf", FontWeight.W300),
    Font("font/oswald/Oswald-Regular.ttf"),
    Font("font/oswald/Oswald-Medium.ttf", FontWeight.W500),
    Font("font/oswald/Oswald-SemiBold.ttf", FontWeight.W600),
    Font("font/oswald/Oswald-Bold.ttf", FontWeight.W700)
)

val FractalThemeTypography = Typography(
    body1 = TextStyle(
        fontFamily = Oswald,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h6 = TextStyle(
        fontFamily = Oswald,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    ),
    h5 = TextStyle(
        fontFamily = Oswald,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Oswald,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp
    ),
)