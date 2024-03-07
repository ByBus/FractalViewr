package domain

import kotlin.math.abs

class CanvasState(
    private var xMin: Double,
    private var xMax: Double,
    private var yMin: Double,
    private var yMax: Double,
) : FractalSpaceState<Double> {
    private val width: Double
        get() = abs(xMax - xMin)

    private val height: Double
        get() = abs(yMax - yMin)

    override fun scaledNear(direction: Float, x: Double, y: Double): CanvasState {
        val multiplier = if (direction < 0) ZOOM_OUT else ZOOM_IN
        val horizontalPart = width * multiplier
        val verticalPart = height * multiplier
        return CanvasState(x - horizontalPart, x + horizontalPart, y - verticalPart, y + verticalPart)
    }

    override fun copy(): CanvasState = CanvasState(xMin, xMax, yMin, yMax)

    override fun shift(deltaX: Double, deltaY: Double) {
        xMin += deltaX
        xMax += deltaX
        yMin += deltaY
        yMax += deltaY
    }

    override fun <R> mapAxis(mapper: (Double, Double) -> R, xAxis: Boolean): R =
        if (xAxis) mapper.invoke(xMin, xMax) else mapper.invoke(yMin, yMax)

    override fun <R> mapSize(mapper: (Double, Double) -> R): R = mapper.invoke(width, height)

    companion object {
        private const val ZOOM_FACTOR = 1.3
        private const val ZOOM_IN = ZOOM_FACTOR * 0.5
        private const val ZOOM_OUT = 1.0 / ZOOM_FACTOR * 0.5
    }
}
