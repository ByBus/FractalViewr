package ui.gradientmaker.controller

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.util.*
import kotlin.math.abs

class GradientSliderController(
    private val colorStart: Color = Color.Red,
    private val colorEnd: Color = Color.Blue,
) {
    private var initialColorPositions = Pair(0f, 1f)
    private val gradientColors = mutableStateListOf(
        GradientColor.Marker(initialColorPositions.first, colorStart),
        GradientColor.Marker(initialColorPositions.second, colorEnd)
    )
    val gradientBackground by derivedStateOf {
        gradientColors
            .sortedBy { it.position }
            .filter { it.state != ColorStopState.REMOVE_CANDIDATE }
            .map { it.position to it.color }
            .toTypedArray()
    }
    private var gradientColor: GradientColor = GradientColor.Empty

    fun gradient(): List<GradientColor.Marker> = gradientColors.toList()

    fun selectBetween(positionMin: Offset, positionMax: Offset, size: Size) {
        val min = calculateMarkerPosition(positionMin, size)
        val max = calculateMarkerPosition(positionMax, size)
        val foundGradientColor = findIn(min, max)
        if (foundGradientColor != gradientColor) {
            deselectAllExcept(foundGradientColor)
        }
        setGradientColor(foundGradientColor)
    }

    private fun findIn(positionMin: Float, positionMax: Float): GradientColor {
        return gradientColors.filter { it.position in positionMin..positionMax }
            .minByOrNull { abs(it.position - (positionMin + positionMax) * 0.5f) }
            ?: GradientColor.Empty
    }

    fun finishOperation() {
        with(gradientColor) {
            setSelection(false)
            changeState(ColorStopState.IDLE)
            forceUpdateGradientList(this)
            setGradientColor(GradientColor.Empty)
        }
    }

    private fun setGradientColor(gradientColor: GradientColor) {
        this.gradientColor = gradientColor
    }

    fun updateColor(color: Color) {
        with(gradientColor) {
            changeColor(color)
            forceUpdateGradientList(this)
        }
    }

    fun updatePosition(position: Offset, size: Size) {
        with(gradientColor) {
            setSelection(true)
            changePosition(calculateMarkerPosition(position, size))
            forceUpdateGradientList(this)
        }
    }

    fun addColorStop(color: Color, position: Offset, size: Size) {
        if (gradientColor is GradientColor.Marker) {
            with(gradientColor) {
                switchSelection()
                if (selection()) updateColor(color) else finishOperation()
            }
        } else {
            val pos = calculateMarkerPosition(position, size)
            gradientColors.add(GradientColor.Marker(pos, color))
        }
    }

    private fun deselectAllExcept(vararg except: GradientColor) {
        gradientColors.filter { it !in except }.forEach { it.setSelection(false) }
    }

    fun requestGradient(): List<Pair<Float, Int>> {
        val gradient =
            gradientColors
                .sortedBy { it.position }
                .distinctBy { it.position }
                .map { it.position to it.color.toArgb() }
                .toMutableList()
        if (gradient.first().first != 0f) {
            gradient.add(0, gradient.first().copy(first = 0f))
        }
        if (gradient.last().first != 1f) {
            gradient.add(gradient.last().copy(first = 1f))
        }
        reset()
        return gradient
    }

    fun reset() {
        with(gradientColors) {
            clear()
            addAll(
                listOf(
                    GradientColor.Marker(initialColorPositions.first, colorStart),
                    GradientColor.Marker(initialColorPositions.second, colorEnd)
                )
            )
            // if (size > 2) removeRange(1, lastIndex)
        }
    }

    private fun forceUpdateGradientList(color: GradientColor) {
        if (color is GradientColor.Marker) {
            gradientColors.remove(color)
            gradientColors.add(color)
        }
    }

    private fun calculateMarkerPosition(position: Offset, size: Size): Float {
        val (minPosition, maxPosition) = sliderMinMax(size)
        val length = maxPosition - minPosition
        return ((position.x.coerceIn(minPosition, maxPosition)/* - minPosition*/) / length).coerceIn(0f, 1f)
    }

    fun calculateGradientPosition(positionX: Float, size: Size): Offset {
        val (minPosition, maxPosition) = sliderMinMax(size)
        val length = maxPosition - minPosition
        return Offset((length * positionX) /*+ minPosition*/, size.height / 2)
    }

    private fun sliderMinMax(size: Size): Offset {
//        val padding = size.height / 2
        val padding = 0f
        return Offset(padding, size.width - padding)
    }

    fun remove(fromScaleOnly: Boolean = false) {
        if (gradientColors.size == 1) return
        if (fromScaleOnly) {
            gradientColor.changeState(ColorStopState.REMOVE_CANDIDATE)
            return
        }
        gradientColors.remove(gradientColor)
        setGradientColor(GradientColor.Empty)
    }
}

sealed class GradientColor {
    open fun setSelection(value: Boolean) = Unit
    open fun selection() = false
    open fun switchSelection() = Unit
    open fun changePosition(value: Float) = Unit
    open fun changeColor(value: Color) = Unit
    open fun changeState(value: ColorStopState) = Unit

    data class Marker(
        var position: Float,
        var color: Color,
        var selected: Boolean = false,
        var state: ColorStopState = ColorStopState.IDLE,
        val id: Long = UUID.randomUUID().mostSignificantBits,
    ) : GradientColor() {
        override fun setSelection(value: Boolean) {
            selected = value
        }

        override fun selection() = selected

        override fun changePosition(value: Float) {
            position = value
        }

        override fun changeColor(value: Color) {
            color = value
        }

        override fun changeState(value: ColorStopState) {
            state = value
        }

        override fun switchSelection() {
            selected = !selected
        }
    }

    object Empty : GradientColor()
}

enum class ColorStopState {
    IDLE, REMOVE_CANDIDATE
}