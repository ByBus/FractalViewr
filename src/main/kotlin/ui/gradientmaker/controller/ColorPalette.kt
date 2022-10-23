package ui.gradientmaker.controller

import ui.gradientmaker.ColorProducer
import java.awt.GradientPaint
import java.awt.Point
import java.awt.image.BufferedImage
import java.awt.Color as AwtColor

class ColorPalette : ColorProducer {
    private val bufferedImage = BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB)
    private var mainColors = listOf(
        AwtColor.RED,
        AwtColor.YELLOW,
        AwtColor.GREEN,
        AwtColor.CYAN,
        AwtColor.BLUE,
        AwtColor.MAGENTA,
        AwtColor.RED
    )

    override fun allColors() = mainColors.toList()

    override fun color(x: Float, y: Float): AwtColor {
        with(bufferedImage) {
            val xImageSpace = ((width - 1) * x).toInt()
            val yImageSpace = ((height - 1) * y).toInt()
            return AwtColor(getRGB(xImageSpace, yImageSpace))
        }
    }

    private fun createInternalPalette() {
        val awtColors = mainColors.map { AwtColor(it.red, it.green, it.blue) }
        val horStep = bufferedImage.width / awtColors.lastIndex
        var i = 0
        while (i < awtColors.lastIndex) {
            val x0 = horStep * i
            val x1 = x0 + horStep
            val gradient = GradientPaint(
                x0.toFloat(), 0f, awtColors[i],
                x1.toFloat(), 0f, awtColors[i + 1], false
            )
            with(bufferedImage) {
                setGradient(gradient, Point(x0, 0), Point(x1, height))
            }
            i++
        }
        addBrightnessGradient()
    }

    private fun addBrightnessGradient() {
        with(bufferedImage) {
            val gradient = GradientPaint(
                0f, 0f, AwtColor.WHITE,
                0f, height.toFloat(), AwtColor(0f, 0f, 0f, 0f), false
            )
            setGradient(gradient, Point(0, 0), Point(width, height))
        }
    }

    private fun BufferedImage.setGradient(gradientPaint: GradientPaint, from: Point, to: Point) {
        val g2 = createGraphics()
        g2.paint = gradientPaint
        g2.fillRect(from.x, from.y, to.x, to.y)
        g2.dispose()
    }

    init {
        createInternalPalette()
    }
}