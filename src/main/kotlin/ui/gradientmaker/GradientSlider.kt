package ui.gradientmaker

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import ui.gradientmaker.controller.GradientSliderController
import ui.gradientmaker.controller.MarkerState
import kotlin.math.abs

private const val SINGLE_CLICK_DURATION = 200L

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GradientSlider(
    gradientController: GradientSliderController,
    pickerRadius: Dp = 12.dp,
    pickerRadiusMin: Dp = 5.dp,
    sliderHeight: Dp = 35.dp,
    strokeColor: Color = MaterialTheme.colors.secondary,
    colorProvider: () -> Color,
) {
    var clickTime by remember { mutableStateOf(0L) }
    var selectedState by remember { mutableStateOf(MarkerState.IDLE) }
    var transitionSelected = updateTransition(selectedState)
    val markerRadiusSelected by transitionSelected.animateDp(
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioMediumBouncy) }
    ) { state ->
        if (state == MarkerState.SELECTED) pickerRadius else pickerRadiusMin
    }
    val markerRadiusDeselected by transitionSelected.animateDp { state ->
        if (state == MarkerState.SELECTED) pickerRadiusMin else pickerRadius
    }
    var runOnce by remember { mutableStateOf(true) }
    Box(
        Modifier.padding(vertical = 8.dp, horizontal = 0.dp)
            .onSizeChanged {
                if (runOnce) {
                    gradientController.updateInitialColorsPositions(it.toSize())
                    runOnce = false
                }
            }
    ) {
        var verticalPosition by remember { mutableStateOf(0f) }
        Canvas(modifier = Modifier.fillMaxWidth().height(sliderHeight)
            .onPointerEvent(PointerEventType.Press) {
                clickTime = System.currentTimeMillis()
            }
            .onPointerEvent(PointerEventType.Release) {
                if (System.currentTimeMillis() - clickTime <= SINGLE_CLICK_DURATION) {
                    val position = it.changes.first().position
                    val (min, max) = clickZoneBorders(position)
                    gradientController.selectBetween(min, max, size.toSize())
                    gradientController.addColorStop(
                        colorProvider(),
                        position,
                        size.toSize()
                    )
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        val (min, max) = clickZoneBorders(it)
                        gradientController.selectBetween(min, max, size.toSize())
                    },
                    onDrag = { change, _ ->
                        gradientController.updatePosition(change.position, size.toSize())
                        val verticalDrag = abs(change.position.y - size.height / 2)
                        when {
                            verticalDrag >= size.height -> gradientController.remove()
                            verticalDrag >= size.height / 2 -> {
                                verticalPosition = change.position.y
                                gradientController.remove(fromScaleOnly = true)
                            }
                        }
                    },
                    onDragEnd = {
                        gradientController.finishOperation()
                        verticalPosition = 0f
                    },
                )
            }.drawBehind {
                val cornerRadius = CornerRadius(sliderHeight.value, sliderHeight.value)
                drawRoundRect(
                    brush = Brush.horizontalGradient(*gradientController.gradientBackground),
                    cornerRadius = cornerRadius,
                )
                drawRoundRect(color = strokeColor, style = Stroke(2f), cornerRadius = cornerRadius)
            }
        ) {

        }
        var currentSelectedId by remember { mutableStateOf(0L) }
        for (gradientColor in gradientController.gradient()) {
            val markerSet = currentSelectedId == gradientColor.id
            when {
                !markerSet && gradientColor.selected -> currentSelectedId = gradientColor.id
                markerSet && gradientColor.selected -> selectedState = MarkerState.SELECTED
                markerSet && !gradientColor.selected -> selectedState = MarkerState.IDLE
            }
            val radius = if (markerSet) markerRadiusSelected else pickerRadiusMin
            val changeVerticalValue =
                { coord: Offset -> if (gradientColor.selected && verticalPosition != 0f) coord.copy(y = verticalPosition) else coord }
            Box() {
                Canvas(modifier = Modifier.fillMaxWidth().height(sliderHeight)) {
                    drawMarker(
                        position = changeVerticalValue(
                            gradientController.calculateGradientPosition(
                                gradientColor.position,
                                size
                            )
                        ),
                        color = gradientColor.color,
                        radius = radius,
                        strokeWidth = 0.dp,
                        drawOuterStroke = gradientColor.selected
                    )
                }
            }
        }
    }
}

private fun clickZoneBorders(
    position: Offset,
    radius: Float = 10f,
): Pair<Offset, Offset> {
    val min = position.copy(x = position.x - radius)
    val max = position.copy(x = position.x + radius)
    return Pair(min, max)
}