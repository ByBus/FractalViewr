package data

import java.awt.Color

class AwtColorInterpolator: Interpolator<Color> {
    private val interpolate = { a: Int, b: Int, fraction: Double -> a * (1.0 - fraction) + b * fraction }

    override fun interpolate(from: Color, to: Color, fraction: Double): Color {
        return Color(
            interpolate(from.red, to.red, fraction).toInt(),
            interpolate(from.green, to.green, fraction).toInt(),
            interpolate(from.blue, to.blue, fraction).toInt()
        )
    }
}