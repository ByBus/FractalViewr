package domain.imageprocessing

import domain.CanvasStateHolder
import domain.FileSaver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin
import java.awt.image.BufferedImage
import java.io.File

class FileSaveDialogController(
    private val configurationProvider: ConfigurationProvider<CanvasStateHolder>,
    private val imageFileSaver: FileSaver<BufferedImage>,
) {
    private var job: Job? = null
    private var subscriptionJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private lateinit var imageProcessor: FractalImageProcessor

    init {
        createImageprocessor(1, 1)
        imageFileSaver.progressProvider { percent -> _progress.value = percent }
    }

    private val _image = MutableStateFlow(imageProcessor.image.value)
    val image = _image.asStateFlow()

    private val _progress = MutableStateFlow(100f)
    val savedPercentage = _progress.asStateFlow()

    fun createImageprocessor(width: Int, height: Int) {
        imageProcessor = getKoin()
            .get(
                qualifier = named("rescale_image_save_dialog"),
                parameters = { parametersOf(width, height) }
            )
        configurationProvider.provideState { fractal, stateHolder ->
            imageProcessor.setConfiguration(fractal, stateHolder)
        }
    }

    fun compute() {
        job?.cancel()
        job = coroutineScope.launch {
            imageProcessor.computeImage()
        }
    }

    fun subscribe() {
        subscriptionJob?.cancel()
        subscriptionJob = coroutineScope.launch {
            imageProcessor.image.collect {
                _image.value = it
            }
        }
    }

    fun update(image: BufferedImageWrapper) {
        _image.value = image
    }

    fun saveImage(file: File) {
        job?.cancel()
        job = coroutineScope.launch {
            imageFileSaver.save(image.value.bufferedImage, file)
        }
    }

    fun cancelJob() {
        job?.cancel()
        subscriptionJob?.cancel()
    }
}