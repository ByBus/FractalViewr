package ui.gradientmaker.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import ui.gradientmaker.ColorProducer

class ColorPickerController(
    private val coordinateConverter: CoordinateConverter,
    private val colorPalette: ColorProducer
) {
    private var mainColors = colorPalette.allColors().map { Color(it.rgb) }
    private var lightnessColors = listOf(Color.White, Color.Transparent)
    private var lightness = 0f
    private var previousMarkerPositionFraction = floatArrayOf(0f, 0f)
    private var previousLightnessPositionFraction = 0.5f

    var mainGradient: List<Color> by mutableStateOf(mainColors)
        private set
    var lightnessGradient by mutableStateOf(lightnessColors)
        private set
    var selectedColor by mutableStateOf(Color.White)
        private set
    var selectedLightness by mutableStateOf(Color.Gray)
        private set
    var markerPosition by mutableStateOf(Offset.Zero)
        private set
    var lightnessPosition by mutableStateOf(Offset(0f, 12f))
        private set

    fun setMarkerColor(position: Offset, center: Offset, paletteWheelRadius: Float) {
        val positionAroundCenter = position - center
        val polar = coordinateConverter.cartesianToPolar(positionAroundCenter.x, positionAroundCenter.y)
        val xFraction = polar.x / 360
        val yFraction = (polar.y / paletteWheelRadius).coerceAtMost(1f)
        saveFractionPosition(xFraction, yFraction)
        setColor(xFraction, yFraction)
        calculateConstrainedMarkerPosition(position, center, paletteWheelRadius)
    }

    fun updateMarkerPosition(size: Offset, paletteWheelRadius: Float) {
        val center = size * 0.5f
        val angle = 360 * previousMarkerPositionFraction[0]
        val r = paletteWheelRadius * previousMarkerPositionFraction[1]
        val position = coordinateConverter.polarToCartesian(angle, r) + center
        calculateConstrainedMarkerPosition(position, center, paletteWheelRadius)
    }

    fun setLightness(position: Offset, size: IntSize) {
        val (minPosition, maxPosition) = sliderMinMax(size)
        lightnessPosition = Offset(position.x.coerceIn(minPosition, maxPosition), minPosition)
        val length = maxPosition - minPosition
        val fraction = ((lightnessPosition.x - minPosition) / length).coerceIn(0f, 1f)
        calculateLightness(fraction)
    }

    fun updateLightnessPosition(size: IntSize) {
        val (minPosition, maxPosition) = sliderMinMax(size)
        val length = maxPosition - minPosition
        lightnessPosition = Offset(
            (length * previousLightnessPositionFraction) + minPosition,
            minPosition
        )
    }

    private fun saveFractionPosition(xFraction: Float, yFraction: Float) {
        previousMarkerPositionFraction[0] = xFraction
        previousMarkerPositionFraction[1] = yFraction
    }

    private fun updateGradients() {
        mainGradient = mainColors.map {
            updateLightness(it)
        }
        lightnessGradient = lightnessColors.map {
            updateLightness(it)
        }
    }

    private fun calculateConstrainedMarkerPosition(
        position: Offset,
        center: Offset,
        paletteWheelRadius: Float
    ) {
        val centerNegativePosition = center - position
        val maxPosition = centerNegativePosition.withDistance(paletteWheelRadius)

        markerPosition = if (centerNegativePosition.getDistanceSquared() <= maxPosition.getDistanceSquared()) {
            Offset.Zero
        } else {
            centerNegativePosition.minus(maxPosition)
        } + position
    }

    private fun setColor(xPosition: Float, yPosition: Float) {
        val rgb = colorPalette.color(xPosition, yPosition).rgb
        selectedColor = updateLightness(Color(rgb))
    }

    private fun calculateLightness(positionFraction: Float, toStart: Float = -1f, toEnd: Float = 1f) {
        previousLightnessPositionFraction = positionFraction
        lightness = toStart + positionFraction * (toEnd - toStart)
        setColor(previousMarkerPositionFraction[0], previousMarkerPositionFraction[1])
        updateGradients()
        selectedLightness = Color(positionFraction, positionFraction, positionFraction)
    }

    private fun sliderMinMax(size: IntSize): Offset {
        val padding = size.height / 2f
        return Offset(padding, size.width - padding)
    }

    private fun updateLightness(color: Color): Color {
        return Color(
            updateChannelLightness(color.red),
            updateChannelLightness(color.green),
            updateChannelLightness(color.blue),
            color.alpha
        )
    }

    private fun updateChannelLightness(channel: Float): Float = (channel + lightness).coerceIn(0f, 1f)
}

private fun Offset.unit(): Offset {
    val dist = getDistance()
    val modulus = if (dist == 0f) 1f else dist
    return Offset(x / modulus, y / modulus)
}

private fun Offset.withDistance(modulus: Float): Offset {
    val unit = unit()
    return Offset(unit.x * modulus, unit.y * modulus)
}