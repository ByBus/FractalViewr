package ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextGradientButton(
    text: String,
    gradient: List<Pair<Float, Color>>,
    onClick: () -> Unit,
    onEdit: () -> Unit = {},
    cornerRadius: Dp = 4.dp,
    showEditButton: Boolean = false,
    modifier: Modifier = Modifier,
) {
    var editButtonVisibility by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(cornerRadius)
    val horPadding = 32.dp
    val verPadding = 8.dp
    Box(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) { editButtonVisibility = true }
            .onPointerEvent(PointerEventType.Exit) { editButtonVisibility = false },
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.body1.copy(
                shadow = Shadow(
                    color = Color.Black,
                    blurRadius = 1f
                )
            ),
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .border(1.dp, MaterialTheme.colors.secondaryVariant, shape)
                .clickable(onClick = onClick)
                .clip(shape)
                .background(brush = Brush.horizontalGradient(*gradient.toTypedArray()))
                .padding(vertical = verPadding, horizontal = horPadding)
                .drawBehind {
                    //top rim glare
                    val top = -verPadding.value + 1f
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(-horPadding.value, top),
                        end = Offset(size.width + horPadding.value, top),
                        strokeWidth = 2f
                    )
                }
        )
        if (showEditButton) {
            AnimatedVisibility(
                visible = editButtonVisibility,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = 8.dp)
            )
            {
                FloatingActionButton(
                    onClick = onEdit,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.size(18.dp)
                ) {
                    Icon(
                        Icons.Rounded.Edit, "", tint = MaterialTheme.colors.onSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

