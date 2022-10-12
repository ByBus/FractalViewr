package domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.CanvasState
import data.CanvasStateHolder
import data.GradientData
import data.GradientRepository
import data.fractal.Fractal
import presenter.NumberRemaper
import presenter.Palette
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage

private const val WIDTH = 1000
private const val HEIGHT = 1000
private const val PREVIEW_WIDTH = 200
private const val PREVIEW_HEIGHT = 200
private const val SCROLL_STEP = 0.6

class FractalManager(
    private val fractal: Fractal,
    private val screenMapper: NumberRemaper<Int, Double>,
    private val canvasState: CanvasStateHolder,
    private val palette: Palette,
    private val gradientRepository: GradientRepository
) {
    init {
        println("INIT")
        setGradient(gradientRepository.gradients[0].colorStops)
    }

    val gradients = gradientRepository.gradients
    var invalidator by mutableStateOf(0)
        private set

    private val previewBuffer = BufferedImage(PREVIEW_WIDTH, PREVIEW_HEIGHT, BufferedImage.TYPE_INT_RGB)
    private val buffer = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
    val image by mutableStateOf(buffer)

    fun computeImage() {
        compute(image)
    }

    fun setScroll(value: Float, x: Int, y: Int) {
        val state = canvasState.state()
        val (x0, y0) = mapToCanvas(x, y, image.width, image.height, state)
        val multiplier = if (value < 0) SCROLL_STEP else 1 / SCROLL_STEP
        canvasState.save(state.scale(multiplier, x0, y0))
    }

    fun computePreview() {
        compute(previewBuffer)
        with(image) {
            data = previewBuffer.upscale(width, height).data
        }
    }

    private fun compute(image: BufferedImage) {
        val state = canvasState.state()
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val (x0, y0) = mapToCanvas(x, y, image.width, image.height, state)
                val value = fractal.calculate(x0, y0)
                val color = palette.getColor(value)
                image.setRGB(x, y, color)
                invalidator++
            }
        }
    }

    private fun mapToCanvas(x: Int, y: Int, width: Int, height: Int, state: CanvasState): Pair<Double, Double> {
        val x0 = screenMapper.reMap(x, 0, width, state.xMin, state.xMax)
        val y0 = screenMapper.reMap(y, 0, height, state.yMin, state.yMax)
        return Pair(x0, y0)
    }

    private fun BufferedImage.upscale(
        targetWidth: Int = buffer.width,
        targetHeight: Int = buffer.height
    ): BufferedImage {
        val resultingImage: Image = getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST)
        return BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB).apply {
            graphics.drawImage(resultingImage, 0, 0, null)
        }
    }

    fun setGradient(gradient: List<Pair<Float, Int>>) {
        palette.setGradient(gradient.map { it.first to Color(it.second) })
    }

    fun saveCurrent() {
        with(canvasState) {
            save(state().copy())
        }
    }

    fun dragCanvas(deltaX: Int, deltaY: Int) {
        with(canvasState.state()) {
            val kX = if (deltaX < 0) -1 else 1
            val kY = if (deltaY < 0) -1 else 1
            val deltaX0 = screenMapper.reMap(kX * deltaX, 0, buffer.width, 0.0, width)
            val deltaY0 = screenMapper.reMap(kY * deltaY, 0, buffer.height, 0.0, height)
            shift(kX * deltaX0, kY * deltaY0)
        }
    }

    fun saveGradient(name: String, colors: List<Pair<Float, Int>>) {
        gradientRepository.save(GradientData(name, colors))
    }

}