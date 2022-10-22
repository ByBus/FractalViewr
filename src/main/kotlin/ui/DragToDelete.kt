package ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DragToDelete(onDelete: () -> Unit, content: @Composable () -> Unit) {
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
        onDelete()
    }
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(
            DismissDirection.StartToEnd
        ),
        modifier = Modifier,
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.05f)
        },
        background = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> MaterialTheme.colors.primary
                    else -> MaterialTheme.colors.error
                }
            )
            val alignment = Alignment.CenterStart
            val icon = Icons.Default.Clear
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.7f else 1.3f
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dp(20f)),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Delete Icon",
                    modifier = Modifier.scale(scale),
                    tint = color
                )
            }
        },
        dismissContent = {
            content()
        }
    )
}