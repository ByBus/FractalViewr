package domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.*
import data.fractal.Mandelbrot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import presenter.Palette
import presenter.RangeRemapper
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File


private const val WIDTH = 1000
private const val HEIGHT = 1000
private const val PREVIEW_WIDTH = 200
private const val PREVIEW_HEIGHT = 200

typealias BiMapper<Double> = (Double, Double) -> Double
typealias BiMapperPair<Double> = (Double, Double) -> Pair<Double, Double>

class FractalManager(
    private val screenMapper: RangeRemapper<Int, Double>,
    private val palette: Palette,
    private val gradientRepository: GradientRepository,
    private val imageFileSaver: FileSaver<BufferedImage>
) {
    private var fractal: Fractal = Mandelbrot()
    private var canvasStateHolder = CanvasStateHolder(CanvasState(-2.0, 1.0, -1.5, 1.5))

    init {
        println("FractalManager INIT")
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

    fun setScroll(direction: Float, x: Int, y: Int) {
        val state = canvasStateHolder.state()
        val (x0, y0) = mapToCanvas(x, y, image.width, image.height, state)
        canvasStateHolder.save(state.scaledNear(direction, x0, y0))
    }

    val coroutineScope = CoroutineScope(Dispatchers.Default)
    fun computePreview() {
        coroutineScope.launch {
            compute(previewBuffer)
            with(image) {
                data = previewBuffer.upscale(width, height).data
            }
        }
    }

    private fun compute(image: BufferedImage) {
        val state = canvasStateHolder.state()
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

    private fun mapToCanvas(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        state: FractalSpaceState<Double>,
    ): Pair<Double, Double> {
        val xMapper: BiMapper<Double> = { xMin, xMax -> screenMapper.reMap(x, 0, width, xMin, xMax) }
        val yMapper: BiMapper<Double> = { yMin, yMax -> screenMapper.reMap(y, 0, height, yMin, yMax) }
        val x0 = state.mapAxis(xMapper, true)
        val y0 = state.mapAxis(yMapper, false)
        return x0 to y0
    }

    private fun BufferedImage.upscale(
        targetWidth: Int = buffer.width,
        targetHeight: Int = buffer.height,
    ): BufferedImage {
        val resultingImage: Image = getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST)
        return BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB).apply {
            graphics.drawImage(resultingImage, 0, 0, null)
        }
    }

    fun setGradient(gradient: List<Pair<Float, Int>>) {
        palette.setGradient(gradient.map { it.first to Color(it.second) })
    }

    fun saveCurrentState() {
        with(canvasStateHolder) {
            save(state().copy())
        }
    }

    fun dragCanvas(deltaX: Int, deltaY: Int) {
        with(canvasStateHolder.state()) {
            val kX = if (deltaX < 0) -1 else 1
            val kY = if (deltaY < 0) -1 else 1
            val xyToCanvasMapper: BiMapperPair<Double> = { width, height ->
                val x = screenMapper.reMap(kX * deltaX, 0, buffer.width, 0.0, width)
                val y = screenMapper.reMap(kY * deltaY, 0, buffer.height, 0.0, height)
                x to y
            }
            val (deltaX0, deltaY0) = mapSize(xyToCanvasMapper)
            shift(kX * deltaX0, kY * deltaY0)
        }
    }

    fun saveGradient(name: String, colors: List<Pair<Float, Int>>) {
        gradientRepository.save(GradientData(name, colors))
    }

    fun undo() {
        canvasStateHolder.removeLast()
        computeImage()
    }

    fun reset() {
        canvasStateHolder.reset()
        computeImage()
    }

    fun setConfiguration(fractal: Fractal, state: CanvasState) {
        this.fractal = fractal
        this.canvasStateHolder = CanvasStateHolder(state)
    }

    fun saveImage(file: File) {
        imageFileSaver.save(buffer, file)
    }
}