package domain.imageprocessing

import domain.FractalSpaceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import presenter.Palette
import presenter.RangeRemapper

class RescaleImageProcessor(
    width: Int,
    height: Int,
    palette: Palette<Int>,
    screenMapper: RangeRemapper<Int, Double>,
) : FractalImageProcessor(width, height, screenMapper, palette) {

    override fun CoroutineScope.computation(state: FractalSpaceState<Double>) {
        println(width)
        val height = buffer.height
        val width = buffer.width
        outer@ for (y in 0 until height) {
            if (!isActive) break@outer
            launch {
                inner@ for (x in 0 until width) {
                    if (!isActive) break@inner
                    computePixel(x, y, state)
                }
                notifyUpdate()
            }
        }
    }
}