package presenter

typealias ColorAwt = java.awt.Color

class Palette(private val size: Int = 1000) {
    private val colors = IntArray(size){0x00421E0F}

    fun getColor(position: Int): Int {
        return colors[position and size - 1]
    }

    fun setGradient(gradient: List<ColorAwt>) {
        val parts = gradient.lastIndex
        val stepsBetweenColors = computeStepsInBetweenColors(parts)
        var position = 0
        var maxIteration = 0
        for (i in 0 until parts) {
            var fraction = 0.0
            val fractionIncrement = 1.0 / stepsBetweenColors[i]
            val colorA = gradient[i]
            val colorB = gradient[i + 1]
            maxIteration += stepsBetweenColors[i]
            while (position < maxIteration) {
                colors[position] = interpolate(colorA, colorB, fraction).rgb
                fraction += fractionIncrement
                position++
            }
        }
    }

    private fun computeStepsInBetweenColors(parts: Int): IntArray {
        val steps = IntArray(parts) { 0 }
        (1..parts).forEach { i ->
            steps[i - 1] += size / parts
        }
        if (size % parts != 0) {
            steps[steps.lastIndex] += size % parts
        }
        return steps
    }

    private fun interpolate(colorA: ColorAwt, colorB: ColorAwt, fraction: Double): ColorAwt {
        return ColorAwt(
            interpolate(colorA.red, colorB.red, fraction).toInt(),
            interpolate(colorA.green, colorB.green, fraction).toInt(),
            interpolate(colorA.blue, colorB.blue, fraction).toInt()
        )
    }
    private val interpolate = { a: Int, b: Int, fraction: Double -> a * (1.0 - fraction) + b * fraction }
}