package data

import domain.FractalSpaceState
import kotlin.math.abs

class CanvasState(
    private var xMin: Double, // X_min
    private var xMax: Double, // X_max
    private var yMin: Double, // Y_min
    private var yMax: Double, // Y_max
    private val zoomStep: Double = 0.6,
) : FractalSpaceState<Double> {
    private val width: Double
        get() = abs(xMax - xMin)

    private val height: Double
        get() = abs(yMax - yMin)

    override fun scaledNear(direction: Float, x: Double, y: Double): CanvasState {
        val multiplier = if (direction < 0) zoomStep else 1 / zoomStep
        val horizontalPart = width * multiplier * 0.5
        val verticalPart = height * multiplier * 0.5
        return CanvasState(x - horizontalPart, x + horizontalPart, y - verticalPart, y + verticalPart, zoomStep)
    }

    override fun copy(): CanvasState = CanvasState(xMin, xMax, yMin, yMax, zoomStep)

    override fun shift(deltaX: Double, deltaY: Double) {
        xMin += deltaX
        xMax += deltaX
        yMin += deltaY
        yMax += deltaY
    }

    override fun <R> mapAxis(mapper: (Double, Double) -> R, xAxis: Boolean): R {
        return if (xAxis) mapper.invoke(xMin, xMax) else mapper.invoke(yMin, yMax)
    }

    override fun <R> mapSize(mapper: (Double, Double) -> R): R = mapper.invoke(width, height)
}
