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
        val height = buffer.height
        val width = buffer.width
        for (y in 0 until height) {
            if (!isActive) break
            launch {
                for (x in 0 until width) {
                    if (!isActive) break
                    computePixel(x, y, state)
                }
                notifyUpdate()
            }
        }
    }
}