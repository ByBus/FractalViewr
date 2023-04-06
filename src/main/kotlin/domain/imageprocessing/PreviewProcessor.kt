package domain.imageprocessing

import domain.FractalSpaceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import domain.Palette
import domain.RangeRemapper

class PreviewProcessor(
    width: Int,
    height: Int,
    palette: Palette<Int>,
    screenMapper: RangeRemapper<Int, Double>,
) : FractalImageProcessor(width, height, screenMapper, palette) {

    override fun CoroutineScope.computation(state: FractalSpaceState<Double>) {

        outer@ for (y in 0 until height) {
            for (x in 0 until width) {
                if (!isActive) break@outer
                computePixel(x, y, state)
            }
        }
        notifyUpdate()
    }
}