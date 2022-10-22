package domain

import data.*
import data.fractal.Mandelbrot
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import presenter.Palette
import presenter.RangeRemapper
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


private const val WIDTH = 1000
private const val HEIGHT = 1000
private const val PREVIEW_WIDTH = 200
private const val PREVIEW_HEIGHT = 200

typealias BiMapper<Double> = (Double, Double) -> Double
typealias BiMapperPair<Double> = (Double, Double) -> Pair<Double, Double>

class FractalManager(
    private val screenMapper: RangeRemapper<Int, Double>,
    private val palette: Palette<Int>,
    private val gradientRepository: GradientRepository,
    private val imageFileSaver: FileSaver<BufferedImage>,
) {
    private var job: Job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private var fractal: Fractal = Mandelbrot()
    private var canvasStateHolder = CanvasStateHolder(CanvasState(-2.0, 1.0, -1.5, 1.5))

    private val previewBuffer = BufferedImage(PREVIEW_WIDTH, PREVIEW_HEIGHT, BufferedImage.TYPE_INT_RGB)
    private val buffer = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)

    val gradients = gradientRepository.gradients

    private val _invalidator = MutableStateFlow(0)
    val invalidator = _invalidator.asStateFlow()

    val image = MutableStateFlow(buffer).asStateFlow()

    private val randomPixelCombinations = preparePixels(buffer.width, buffer.height)


    fun setScroll(direction: Float, x: Int, y: Int) {
        val state = canvasStateHolder.state()
        val (x0, y0) = mapToCanvas(x, y, buffer.width, buffer.height, state)
        canvasStateHolder.save(state.scaledNear(direction, x0, y0))
        computePreviewAndThenImage()
    }

    fun computeImage(withPreviewFirst: Boolean = true) {
        job.cancel()
        job = coroutineScope.launch {
            if (withPreviewFirst) fulfillBufferWithPreview()
            computeRandomPixels()
        }
    }

    private fun computePreview() {
        job = coroutineScope.launch {
            computeSequentialPixels(previewBuffer)
            if (isActive) fulfillBufferWithPreview()
        }
    }

    private fun fulfillBufferWithPreview() {
        with(buffer) {
            data = previewBuffer.upscale(width, height).data
        }
    }

    private fun computePreviewAndThenImage() {
        job.cancel()
        job = coroutineScope.launch {
            computeSequentialPixels(previewBuffer)
            fulfillBufferWithPreview()
            computeRandomPixels()
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun CoroutineScope.computeRandomPixels(image: BufferedImage = buffer) {
        val state = canvasStateHolder.state()
        val (_, duration) = measureTimedValue {
            randomPixelCombinations.forEach { chunk ->
                launch {
                    for (i in chunk.indices step 2) {
                        if (!isActive) return@launch
                        compute(chunk[i], chunk[i + 1], image, state)
                    }
                    _invalidator.value--
                }
            }
        }
        println("Generation time is $duration")
    }

    private fun CoroutineScope.computeSequentialPixels(image: BufferedImage) {
        val state = canvasStateHolder.state()
        outer@ for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                if (!isActive) break@outer
                compute(x, y, image, state)
            }
        }
        _invalidator.value++
    }

    private fun compute(
        x: Int,
        y: Int,
        image: BufferedImage,
        state: FractalSpaceState<Double>,
    ) {
        val (x0, y0) = mapToCanvas(x, y, image.width, image.height, state)
        val value = fractal.calculate(x0, y0)
        val color = palette.color(value)
        image.setRGB(x, y, color)
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
        palette.setColors(gradient)
        computeImage(withPreviewFirst = false)
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
            computePreview()
        }
    }

    fun saveGradient(name: String, colors: List<Pair<Float, Int>>) {
        gradientRepository.save(GradientData(name, colors))
    }

    fun undo() {
        canvasStateHolder.removeLast()
        computeImage(withPreviewFirst = false)
    }

    fun reset() {
        canvasStateHolder.reset()
        computeImage(withPreviewFirst = false)
    }

    fun setConfiguration(fractal: Fractal, state: FractalSpaceState<Double>) {
        this.fractal = fractal
        this.canvasStateHolder = CanvasStateHolder(state)
        computePreviewAndThenImage()
    }

    fun saveImage(file: File) {
        imageFileSaver.save(buffer, file)
    }

    private fun preparePixels(width: Int, height: Int) = (0 until height).asSequence()
        .cartesianProduct((0 until width).asSequence())
        .toList()
        .shuffled()
        .chunked(1000) { it ->
            it.flatten().toIntArray()
        }.toTypedArray()

    fun delete(gradient: GradientData) {
        gradientRepository.delete(gradient)
    }

    init {
        setGradient(gradients.value[0].colorStops)
    }
}

private fun <T> Sequence<T>.cartesianProduct(other: Sequence<T>) = this.flatMap { item1 ->
    other.map { item2 ->
        listOf(item1, item2)
    }
}

