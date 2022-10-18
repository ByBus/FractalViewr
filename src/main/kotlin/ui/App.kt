package ui

import Localization
import androidx.compose.animation.*
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import domain.*
import ui.gradientmaker.controller.ColorPickerController
import ui.gradientmaker.controller.GradientSliderController
import ui.style.FractalTheme

private const val WIDTH = 1000
private const val HEIGHT = 1000

@Composable
@Preview
fun App(
    fractalManager: FractalManager,
    fractalFactory: FractalFactory,
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
            var currentFractal: FractalType by remember { mutableStateOf(MainFractals.MANDELBROT) }
            Column(modifier = Modifier) {
                ToolBar(openDialog, fractalManager)
                DropdownMenuSelector(
                    MainFractals.values().map { it.title() },
                    label = Localization.fractalSelectorTitle
                ) {
                    currentFractal = MainFractals.values()[it]
                    fractalFactory.changeConfiguration(currentFractal)
                }
                AppearanceAnimated(currentFractal == MainFractals.JULIA) {
                    DropdownMenuSelector(JuliaFamily.values().map { it.title() }, label = Localization.juliaconstant) {
                        fractalFactory.changeConfiguration(JuliaFamily.values()[it])
                    }
                }
                GradientButtons(fractalManager)
            }
        }
    }
}

@Composable
private fun ColumnScope.AppearanceAnimated(
    visibility: Boolean,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = visibility,
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        content()
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FractalViewPort(fractalManager: FractalManager) {
    val canvasImg = fractalManager.image
    var dragInitPosition = remember { Offset(0f, 0f) }
    Canvas(modifier = Modifier
        .width(canvasImg.width.dp)
        .height(canvasImg.height.dp)
        .onPointerEvent(PointerEventType.Scroll) {
            with(it.changes.first()) {
                fractalManager.setScroll(scrollDelta.y, position.x.toInt(), position.y.toInt())
                fractalManager.computePreviewAndThenImage()
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
                    dragInitPosition = change.position
                },
                onDragEnd = {
                    fractalManager.computeImage()
                }
            )
        }
    ) {
        fractalManager.invalidator.let {
            drawImage(image = canvasImg.toComposeImageBitmap())
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GradientButtons(fractalManager: FractalManager) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(items = fractalManager.gradients, key = { it.id }
        ) { gradient ->
            Row(
                modifier = Modifier.animateItemPlacement(
                    spring(dampingRatio = Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
                )
            ) {
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
        ToolBarIconButton(UndoIcon(), Localization.undo) { fractalManager.undo() }
        ToolBarIconButton(ResetIcon(), Localization.reset) { fractalManager.reset() }
        ToolBarIconButton(SaveIconOutlined(), Localization.save) { showFileSaveDialog = true }
        ToolBarIconButton(AddGradientIcon(), Localization.create) { openDialog.value = true }
    }
    if (showFileSaveDialog) {
        FileSaveDialog(
            title = Localization.fileSaveDialogTitle,
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