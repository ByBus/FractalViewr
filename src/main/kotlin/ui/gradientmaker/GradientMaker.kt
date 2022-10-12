package ui.gradientmaker

import androidx.compose.animation.core.Spring.StiffnessMedium
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.gradientmaker.controller.ColorPickerController
import ui.gradientmaker.controller.GradientSliderController
import ui.gradientmaker.controller.MarkerState

@Composable
fun GradientMaker(
    colorPickerController: ColorPickerController,
    gradientController: GradientSliderController,
    pickerRadius: Dp = 15.dp,
    sliderPickerRadius: Dp = 15.dp,
    strokeColor: Color = MaterialTheme.colors.secondary,
    sliderHeight: Dp = 35.dp,
    modifier: Modifier = Modifier.width(300.dp)
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        ColorPicker(
            pickerRadius = pickerRadius,
            controller = colorPickerController,
            strokeColor = strokeColor,
            sliderHeight = sliderHeight,
            sliderPickerRadius = sliderPickerRadius,
            colorConsumer = { color -> gradientController.updateColor(color) }
        )
        GradientSlider(
            gradientController = gradientController,
            colorProvider = { colorPickerController.selectedColor }
        )
    }
}

@Composable
private fun ColorPicker(
    pickerRadius: Dp = 15.dp,
    pickerRadiusMin: Dp = 10.dp,
    controller: ColorPickerController,
    strokeColor: Color = MaterialTheme.colors.secondary,
    sliderHeight: Dp,
    sliderPickerRadius: Dp,
    colorConsumer: (Color) -> Unit = {},
) {
    var dragging by remember { mutableStateOf(false) }
    var sizeUpdated by remember { mutableStateOf(Offset.Zero) }
    var pickerDragState by remember { mutableStateOf(MarkerState.IDLE) }
    val transitionPickerMarker = updateTransition(pickerDragState)
    val pickerMarkerRadius by transitionPickerMarker.animateDp(
        transitionSpec = { spring(stiffness = StiffnessMedium, dampingRatio = 0.4f) }
    ) { state ->
        if (state == MarkerState.SELECTED) pickerRadius else pickerRadiusMin
    }
    Canvas(
        modifier = Modifier.fillMaxWidth().height(sizeUpdated.x.dp)
            .onSizeChanged {
                if (dragging.not()) {
                    sizeUpdated = Offset(it.width.toFloat(), it.height.toFloat())
                    val maxDistance = sizeUpdated.x / 2 - pickerRadius.value - 6
                    controller.updateMarkerPosition(sizeUpdated, maxDistance)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragging = true
                        pickerDragState = MarkerState.SELECTED
                    },
                    onDrag = { change, _ ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val maxDistance = size.width / 2 - pickerRadius.value - 6
                        controller.setMarkerColor(change.position, center, maxDistance)
                        colorConsumer.invoke(controller.selectedColor)
                    },
                    onDragEnd = {
                        dragging = false
                        pickerDragState = MarkerState.IDLE
                    }
                )
            }
    ) {
        val circleRadius = size.width / 2 - pickerRadius.value
        drawCircle(
            brush = Brush.sweepGradient(controller.mainGradient),
            radius = circleRadius - 2,
        )
        drawCircle(
            brush = Brush.radialGradient(controller.lightnessGradient),
            radius = circleRadius,
        )
        drawCircle(
            color = strokeColor,
            style = Stroke(4f),
            radius = circleRadius
        )
        drawMarker(position = controller.markerPosition, color = controller.selectedColor, radius = pickerMarkerRadius)
    }
    LightnessSlider(
        controller,
        sliderPickerRadius,
        pickerRadiusMin,
        sliderHeight,
        strokeColor,
        colorConsumer
    )
}

@Composable
private fun LightnessSlider(
    controller: ColorPickerController,
    pickerRadius: Dp,
    pickerRadiusMin: Dp,
    sliderHeight: Dp,
    strokeColor: Color,
    colorConsumer: (Color) -> Unit,
) {
    val lightnessGradient = listOf(Color.Black, Color.White)
    var dragging by remember { mutableStateOf(false) }
    var lightnessMarkerDragState by remember { mutableStateOf(MarkerState.IDLE) }
    val transitionLightnessMarker = updateTransition(lightnessMarkerDragState)
    val lightnessMarkerRadius by transitionLightnessMarker.animateDp(
        transitionSpec = { spring(stiffness = StiffnessMedium, dampingRatio = 0.4f) }
    ) { state ->
        if (state == MarkerState.SELECTED) pickerRadius else pickerRadiusMin
    }
    Canvas(modifier = Modifier.fillMaxWidth().height(sliderHeight)
        .onSizeChanged {
            if (dragging.not()) {
                controller.updateLightnessPosition(it)
            }
        }.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = {
                    if ((it - controller.lightnessPosition).getDistance() <= pickerRadius.value) {
                        dragging = true
                        lightnessMarkerDragState = MarkerState.SELECTED
                    }
                },
                onDrag = { change, _ ->
                    if (dragging) {
                        controller.setLightness(change.position, size)
                        colorConsumer.invoke(controller.selectedColor)
                    }
                },
                onDragEnd = {
                    dragging = false
                    lightnessMarkerDragState = MarkerState.IDLE
                }
            )
        }
    ) {
        val radius = (sliderHeight / 2f).value
        drawRoundRect(
            brush = Brush.horizontalGradient(lightnessGradient),
            cornerRadius = CornerRadius(radius, radius)
        )
        drawRoundRect(color = strokeColor, style = Stroke(2f), cornerRadius = CornerRadius(radius, radius))
        drawMarker(
            position = controller.lightnessPosition,
            color = controller.selectedLightness,
            radius = lightnessMarkerRadius
        )
    }
}


