package ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Modifier.shakeOnDrag(offsetX: Animatable<Float, AnimationVector1D>, coroutineScope: CoroutineScope): Modifier =
    composed {
        offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState {},
                onDragStarted = {
                    coroutineScope.launch {
                        offsetX.snapTo(0f)
                        offsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = keyframes {
                                durationMillis = 600
                                -8f at 80
                                8f at 160
                                -8f at 240
                                8f at 320
                                -5f at 400
                                5f at 500
                            }
                        )
                    }
                }
            )
    }