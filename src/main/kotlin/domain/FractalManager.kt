package domain

import domain.imageprocessing.Configurable
import domain.imageprocessing.ConfigurationProvider
import domain.imageprocessing.FractalImageProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


private const val WIDTH = 1000
private const val HEIGHT = 1000

typealias BiMapper<Double> = (Double, Double) -> Double
typealias BiMapperPair<Double> = (Double, Double) -> Pair<Double, Double>
typealias CanvasSpaceStateHolder = StateRepository<FractalSpaceState<Double>>

class FractalManager(
    private val screenMapper: RangeRemapper<Int, Double>,
    private val palette: Palette<Int>,
    private val gradientRepository: CrudRepository<GradientData>,
    private val fractalRepository: FractalFamilyRepository,
    private val finalImageProcessor: FractalImageProcessor,
    private val previewImageProcessor: FractalImageProcessor,
    private var configuration: Configuration
) : Configurable<FractalSpaceState<Double>>, ConfigurationProvider<CanvasSpaceStateHolder> {
    private var job: Job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _image = MutableStateFlow(finalImageProcessor.image.value)
    val image = _image.asStateFlow()
    val gradients = gradientRepository.gradients

    private val _fractalFamily = MutableStateFlow(FractalFamilyUiState())
    val fractalFamily = _fractalFamily.asStateFlow()

    init {
        previewImageProcessor.setConfiguration(configuration.fractal, configuration.canvasStateHolder)
        finalImageProcessor.setConfiguration(configuration.fractal, configuration.canvasStateHolder)
        subscribeToPreview()
        subscribeToFinalImage()
        setGradient(gradients.value[0].colorStops)
    }

    private fun stopImageComputation() {
        job.cancel()
    }

     private fun subscribeToPreview() {
        coroutineScope.launch {
            previewImageProcessor.image.collect { preview ->
                if (preview.ignore) return@collect
                _image.value = preview.upscale(WIDTH, HEIGHT)
            }
        }
    }

    private fun subscribeToFinalImage() {
        coroutineScope.launch {
            finalImageProcessor.image.collect { finalImage ->
                _image.value = finalImage
            }
        }
    }

    private fun updateFromPreview() {
        finalImageProcessor.update(previewImageProcessor.image.value)
    }

    fun setFractalFamilyOf(fractalType: FractalType) {
        if (fractalType.hasFamilyOfFractals()) {
            _fractalFamily.value = FractalFamilyUiState(
                fractalRepository.familyFractalTypes(fractalType).toList(),
                fractalRepository.familyName(fractalType)
            )
        }
    }

    fun setScroll(direction: Float, x: Int, y: Int) {
        stopImageComputation()
        val state = configuration.canvasStateHolder.state()
        val xMapper: BiMapper<Double> = axisMapper(x, 0, WIDTH)
        val yMapper: BiMapper<Double> = axisMapper(y, 0, HEIGHT)
        val x0 = state.mapAxis(xMapper, true)
        val y0 = state.mapAxis(yMapper, false)
        configuration.save(state.scaledNear(direction, x0, y0))
        computePreviewAndThenImage()
    }

    fun computeImage(updateFromPreviewFirst: Boolean = true) {
        stopImageComputation()
        job = coroutineScope.launch {
            if (updateFromPreviewFirst)
                updateFromPreview()
            finalImageProcessor.computeImage()
        }
    }

    private fun computePreview() {
        job = coroutineScope.launch {
            previewImageProcessor.computeImage()
        }
    }

    private fun computePreviewAndThenImage() {
        stopImageComputation()
        job = coroutineScope.launch {
            previewImageProcessor.computeImage()
            updateFromPreview()
            finalImageProcessor.computeImage()
        }
    }

    private val axisMapper: (Int, Int, Int) -> BiMapper<Double> = { value: Int, fromStart: Int, fromEnd: Int ->
        { min, max -> screenMapper.reMap(value, fromStart, fromEnd, min, max) }
    }

    fun setGradient(gradient: List<Pair<Float, Int>>) {
        palette.setColors(gradient)
        computeImage(false)
    }

    fun saveCurrentState() {
        with(configuration) {
            save(state().copy())
        }
    }

    fun dragCanvas(deltaX: Int, deltaY: Int) {
        with(configuration.state()) {
            val kX = if (deltaX < 0) -1 else 1
            val kY = if (deltaY < 0) -1 else 1
            val xyToCanvasValueMapper: BiMapperPair<Double> = { width, height ->
                val x = screenMapper.reMap(kX * deltaX, 0, WIDTH, 0.0, width)
                val y = screenMapper.reMap(kY * deltaY, 0, HEIGHT, 0.0, height)
                x to y
            }
            val (deltaX0, deltaY0) = mapSize(xyToCanvasValueMapper)
            shift(kX * deltaX0, kY * deltaY0)
            computePreview()
        }
    }

    fun saveGradient(name: String, colors: List<Pair<Float, Int>>) {
        gradientRepository.save(GradientData(name, colors))
    }

    fun undo() {
        configuration.removeLast()
        computeImage(false)
    }

    fun reset() {
        configuration.reset()
        computeImage(false)
    }

    override fun setConfiguration(fractal: Fractal, state: FractalSpaceState<Double>) {
        configuration = configuration.copyWith(fractal, state)
        finalImageProcessor.setConfiguration(fractal, configuration)
        previewImageProcessor.setConfiguration(fractal, configuration)
        computePreviewAndThenImage()
    }

    fun delete(gradient: GradientData) {
        gradientRepository.delete(gradient)
    }

    fun editGradient(id: Int, name: String, colors: List<Pair<Float, Int>>) {
        gradientRepository.edit(GradientData(name, colors, id))
    }

    override fun provideState(stateConsumer: (fractal: Fractal, state: CanvasSpaceStateHolder) -> Unit) {
        stateConsumer.invoke(configuration.fractal, configuration.canvasStateHolder)
    }
}

