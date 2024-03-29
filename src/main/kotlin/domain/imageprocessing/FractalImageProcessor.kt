package domain.imageprocessing

import domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage

typealias State = FractalSpaceState<Double>

abstract class FractalImageProcessor(
    val width: Int,
    val height: Int,
    private val screenMapper: RangeRemapper<Int, Double>,
    private val palette: Palette<Int>,
) : ImageProcessor, Configurable<State> {
    protected val buffer = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    private val mutableImage = MutableStateFlow(BufferedImageWrapper(buffer, true))
    val image = mutableImage.asStateFlow()

    protected lateinit var fractal: Fractal
    private lateinit var canvasStateHolder: StateHolder<State>

    open fun update(imageWrapper: BufferedImageWrapper) {
        mutableImage.value = imageWrapper.upscale(width, height)
    }

    override suspend fun computeImage() {
        withContext(Dispatchers.Default) {
            computation(canvasStateHolder.state())
        }
    }

    protected fun notifyUpdate() {
        mutableImage.value = BufferedImageWrapper(buffer)
    }

    abstract fun CoroutineScope.computation(state: FractalSpaceState<Double>)

    override fun setConfiguration(configuration: Configuration<State>) {
        this.fractal = configuration.fractal
        this.canvasStateHolder = configuration.canvasStateHistoryStack
    }

    protected fun computePixel(
        x: Int,
        y: Int,
        state: FractalSpaceState<Double>,
    ) {
        val (x0, y0) = mapPixelToCanvasValues(x, y, buffer.width, buffer.height, state)
        val value = fractal.calculate(x0, y0)
        val color = palette.color(value)
        buffer.setRGB(x, y, color)
    }

    private fun mapPixelToCanvasValues(
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
}