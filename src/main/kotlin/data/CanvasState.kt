package data

import kotlin.math.abs

class CanvasState(
    var xMin: Double, // X_min
    var xMax: Double, // X_max
    var yMin: Double, // Y_min
    var yMax: Double, // Y_max
) {
    val width: Double
        get() = abs(xMax - xMin)

    val height: Double
        get() = abs(yMax - yMin)

    fun scale(value: Double, x: Double, y: Double): CanvasState {
        val horizontalPart = width * value * 0.5
        val verticalPart = height * value * 0.5
        return CanvasState(x - horizontalPart, x + horizontalPart, y - verticalPart, y + verticalPart)
    }

    fun copy(): CanvasState = CanvasState(xMin, xMax, yMin, yMax)

    fun shift(deltaX: Double, deltaY: Double) {
        xMin += deltaX
        xMax += deltaX
        yMin += deltaY
        yMax += deltaY
    }
}