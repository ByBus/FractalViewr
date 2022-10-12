package ui.gradientmaker.controller

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CoordinateConverter {
    fun cartesianToPolar(x: Float, y: Float): Offset {
        val theta = Math.toDegrees(atan2(y, x).toDouble()).toFloat()
        val angleDeg = if (theta > 0) theta else (360 + theta) % 360
        val radius = sqrt(x * x + y * y)
        return Offset(angleDeg, radius)
    }

    fun polarToCartesian(angle: Float, radius: Float): Offset {
        val angleInRadians = Math.toRadians(angle.toDouble())
        val x = (radius * cos(angleInRadians)).toFloat()
        val y = (radius * sin(angleInRadians)).toFloat()
        return Offset(x, y)
    }
}