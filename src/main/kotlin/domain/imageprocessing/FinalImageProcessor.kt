package domain.imageprocessing

import domain.FractalSpaceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import domain.Palette
import domain.RangeRemapper

class FinalImageProcessor(
    width: Int,
    height: Int,
    palette: Palette<Int>,
    screenMapper: RangeRemapper<Int, Double>,
) : FractalImageProcessor(width, height, screenMapper, palette) {
    private val randomPixelCombinationsChunks: List<IntArray> = preparePixels(width, height)
    override fun CoroutineScope.computation(state: FractalSpaceState<Double>) {
        randomPixelCombinationsChunks.forEach { chunk ->
            launch {
                for (i in chunk.indices step 2) {
                    if (!isActive) return@launch
                    computePixel(chunk[i], chunk[i + 1], state)
                }
                notifyUpdate()
            }
        }
    }

    override fun update(imageWrapper: BufferedImageWrapper) {
        if (imageWrapper.ignore) return
        buffer.data = imageWrapper.upscale(width, height).bufferedImage.data
    }

    private fun preparePixels(width: Int, height: Int) = (0 until height).asSequence()
        .cartesianProduct((0 until width).asSequence())
        .shuffled()
        .chunked(1000) { listOfListInt ->
            listOfListInt.flatten().toIntArray()
        }.toList()

    private fun <T> Sequence<T>.cartesianProduct(other: Sequence<T>) = this.flatMap { item1 ->
        other.map { item2 ->
            listOf(item1, item2)
        }
    }
}