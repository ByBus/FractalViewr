package presenter

import data.Interpolator

typealias ColorAwt = java.awt.Color

class Palette(private val size: Int = 255, private val interpolator: Interpolator<ColorAwt>) {
    private val colors = IntArray(size){0x00421E0F}

    fun getColor(position: Int): Int {
        return colors[position and size - 1]
    }

    fun setGradient(gradient: List<Pair<Float, ColorAwt>>) {
        val parts = gradient.lastIndex
        val stepsBetweenColors = computeStepsInBetweenColors(gradient, parts)
        var position = 0
        var maxIteration = 0
        for (i in 0 until parts) {
            var fraction = 0.0
            val fractionIncrement = 1.0 / stepsBetweenColors[i]
            val colorA = gradient[i].second
            val colorB = gradient[i + 1].second
            maxIteration += stepsBetweenColors[i]
            while (position < maxIteration) {
                colors[position] = interpolator.interpolate(colorA, colorB, fraction).rgb
                fraction += fractionIncrement
                position++
            }
        }
    }
    private fun computeStepsInBetweenColors(gradient: List<Pair<Float, ColorAwt>>, parts: Int): IntArray {
        val steps = IntArray(parts) { 0 }
        (1..parts).forEach { i ->
            val partFraction = gradient[i].first - gradient[i - 1].first
            steps[i - 1] += (size * partFraction).toInt()
        }
        steps[steps.lastIndex] += size - steps.sum()
        return steps
    }
}