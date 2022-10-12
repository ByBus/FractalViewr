package ui.gradientmaker

import java.awt.Color

interface ColorProducer {
    fun allColors(): List<Color>
    fun color(x: Float, y: Float): Color
}