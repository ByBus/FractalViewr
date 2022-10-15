package ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.FractalManager
import ui.gradientmaker.controller.ColorPickerController
import ui.gradientmaker.controller.GradientSliderController
import ui.style.FractalTheme

private const val WIDTH = 1000
private const val HEIGHT = 1000

@Composable
@Preview
fun App(
    fractalManager: FractalManager,
    colorPickerController: ColorPickerController,
    gradientSliderController: GradientSliderController,
) {
    FractalTheme {
        Row(modifier = Modifier) {
            FractalViewPort(fractalManager)
            val openDialog = remember { mutableStateOf(false) }
            GradientMakerDialog(openDialog, colorPickerController, gradientSliderController) { name, colors ->
                fractalManager.saveGradient(name, colors)
            }
            Column(modifier = Modifier) {
                ToolBar(openDialog, fractalManager)
                DropdownMenuSelector(listOf("Mandelbrot", "Julia", "Sierpiński carpet"), label = "Fractal"){
                    println("Selected id = $it")
                }
                GradientButtons(fractalManager)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FractalViewPort(fractalManager: FractalManager) {
    val canvasImg = fractalManager.image
    var invalidate by remember { mutableStateOf(0) }
    var dragInitPosition = remember { Offset(0f, 0f) }
    Canvas(modifier = Modifier
        .width(canvasImg.width.dp)
        .height(canvasImg.height.dp)
        .onPointerEvent(PointerEventType.Scroll) {
            with(it.changes.first()) {
                fractalManager.setScroll(scrollDelta.y, position.x.toInt(), position.y.toInt())
                fractalManager.computePreview()
                invalidate--
                fractalManager.computeImage()
            }
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = {
                    dragInitPosition = it
                    fractalManager.saveCurrentState()
                },
                onDrag = { change, _ ->
                    val dragDelta = dragInitPosition - change.position
                    fractalManager.dragCanvas(dragDelta.x.toInt(), dragDelta.y.toInt())
                    fractalManager.computePreview()
                    invalidate++
                    dragInitPosition = change.position
                },
                onDragEnd = {
                    invalidate--
                    fractalManager.computeImage()
                }
            )
        }
    ) {
        invalidate.let {
            drawImage(image = canvasImg.toComposeImageBitmap())
        }
    }
    SideEffect {
        println("recomposition")
        fractalManager.computePreview()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GradientButtons(fractalManager: FractalManager) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(items = fractalManager.gradients, key = {it.id}
        ) { gradient ->
            Row(modifier = Modifier.animateItemPlacement(
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium))) {
                TextGradientButton(
                    text = gradient.name,
                    gradient = gradient.colorStops.map { it.first to Color(it.second) },
                    onClick = {
                        fractalManager.setGradient(gradient.colorStops)
                        fractalManager.computeImage()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ToolBar(openDialog: MutableState<Boolean>, fractalManager: FractalManager) {
    var showFileSaveDialog by remember { mutableStateOf(false) }
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
    ) {
        ToolBarIconButton(UndoIcon(), "Undo") { fractalManager.undo()}
        ToolBarIconButton(ResetIcon(), "Reset") { fractalManager.reset()}
        ToolBarIconButton(SaveIconOutlined(), "Save") { showFileSaveDialog = true }
        ToolBarIconButton(AddGradientIcon(), "Create") { openDialog.value = true }
    }
    if (showFileSaveDialog) {
        FileSaveDialog(
            title = "Select file or type filename",
            onResult = {
                it?.let {
                    fractalManager.saveImage(it)
                }
                showFileSaveDialog = false
            }
        )
    }
}

@Composable
private fun RowScope.ToolBarIconButton(
    icon: Painter,
    text: String,
    action: () -> Unit,
) {
    BottomNavigationItem(
        icon = { Icon(painter = icon, contentDescription = null, modifier = Modifier.size(32.dp)) },
        label = {
            Text(
                text, maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        selected = false,
        onClick = action
    )
}