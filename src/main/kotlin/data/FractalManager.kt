package data

import androidx.compose.runtime.mutableStateOf
import data.fractal.Fractal
import presenter.Palette
import presenter.ScreenMapper
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage

private const val WIDTH = 500
private const val HEIGHT = 500
private const val PREVIEW_WIDTH = 300
private const val PREVIEW_HEIGHT = 300

class FractalManager(
    private val fractal: Fractal,
    private val screenMapper: ScreenMapper,
    private val canvasState: CanvasStateHolder,
    private val palette: Palette,
    gradientRepository: GradientRepository
) {
    init {
        println("FM init")
        //palette.setGradient(gradients[0].second.map { Color(it) })
    }

    val gradients = gradientRepository.gradients

    private val previewBuffer = BufferedImage(PREVIEW_WIDTH, PREVIEW_HEIGHT, BufferedImage.TYPE_INT_RGB)
    private val buffer = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
    val image = mutableStateOf(buffer)


    fun computeImage() {
        compute(buffer)
    }

    fun computePreview() {
        compute(previewBuffer)
        with(buffer) {
            data = previewBuffer.upscale(width, height).data
        }
    }

    private fun compute(image: BufferedImage) {
        screenMapper.setScreenSize(image.width, image.height)
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val state = canvasState.state()
                val x0 = screenMapper.reMapWidth(x, state.xMin, state.xMax)
                val y0 = screenMapper.reMapHeight(y, state.yMin, state.yMax)
                val value = fractal.calculate(x0, y0)
                val color = palette.getColor(value)
                image.setRGB(x, y, color)
            }
        }
    }

    private fun BufferedImage.upscale(targetWidth: Int = buffer.width, targetHeight: Int = buffer.height): BufferedImage {
        val resultingImage: Image = getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST)
        return BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB).apply {
            graphics.drawImage(resultingImage, 0, 0, null)
        }
    }

    fun setGradient(gradient: List<Int>) {
        palette.setGradient(gradient.map { Color(it) })
    }
}