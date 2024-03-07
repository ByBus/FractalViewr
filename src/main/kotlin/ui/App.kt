package ui

import Localization
import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import domain.GradientData
import domain.*
import org.koin.java.KoinJavaComponent.getKoin
import ui.style.FractalTheme


@Composable
@Preview
fun App(
    fractalManager: FractalManager,
    configurator: Configurator,
) {
    FractalTheme {
        val defaultName = Localization.gradientDefaultName
        var currentFractal: FractalType by remember { mutableStateOf(configurator.initialType) }
        var editDialogConfig by remember { mutableStateOf(GradientDialogConfig(name = defaultName)) }

        val fractalFamily by fractalManager.fractalFamily.collectAsState()
        if (editDialogConfig.isOpened()) {
            GradientMakerDialog(
                title = Localization.gradientMakerTitle,
                colorPickerController = getKoin().get(),
                gradientSliderController = getKoin().get(),
                defaultGradientName = editDialogConfig.name,
                startGradient = editDialogConfig.gradient,
                selfClosing = { editDialogConfig = editDialogConfig.withName(defaultName).close() }
            ) { name, colors ->
                when {
                    editDialogConfig.isCreationMode() -> fractalManager.saveGradient(name, colors)
                    editDialogConfig.isEditMode() -> fractalManager.editGradient(id = editDialogConfig.id, name, colors)
                    else -> {}
                }
            }
        }
        Row(modifier = Modifier) {
            FractalViewPort(fractalManager)
            Column(modifier = Modifier) {
                ToolBar(fractalManager, switchToCreateMode = { editDialogConfig = editDialogConfig.create() })
                DropdownMenuSelector(
                    items = currentFractal.familyTypes().map { it.title().uppercase() },
                    label = Localization.fractalSelectorTitle
                ) {
                    currentFractal = currentFractal.familyTypes()[it]
                    configurator.changeConfiguration(currentFractal)
                }
                AppearanceAnimated(currentFractal.hasFamilyOfFractals) {
                    DropdownMenuSelector(
                        items = fractalFamily.types.map { it.title() },
                        label = fractalFamily.name,
                        onSelect = { position ->
                            configurator.changeConfiguration(fractalFamily.types[position])
                        }
                    )
                }
                GradientButtons(fractalManager,
                    onEdit = { id, name, gradient ->
                        editDialogConfig = GradientDialogConfig(id, name, gradient).edit()
                    })
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
            with(density) { -40.dp.roundToPx() }
        } + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        content()
    }
}

@Composable
private fun BoxScope.AppearanceAnimated(
    visibility: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visibility,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        content()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FractalViewPort(fractalManager: FractalManager) {
    val canvasImg by fractalManager.image.collectAsState()
    var dragInitPosition = remember { Offset(0f, 0f) }
    Canvas(modifier = Modifier
        .requiredWidth(canvasImg.bufferedImage.width.dp)
        .requiredHeight(canvasImg.bufferedImage.height.dp)
        .onPointerEvent(PointerEventType.Scroll) {
            with(it.changes.first()) {
                fractalManager.setScroll(scrollDelta.y, position.x.toInt(), position.y.toInt())
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
                    dragInitPosition = change.position
                },
                onDragEnd = {
                    fractalManager.computeImage()
                }
            )
        }
    ) {
        drawImage(image = canvasImg.bufferedImage.toComposeImageBitmap())
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GradientButtons(
    fractalManager: FractalManager,
    onEdit: (Int, String, List<Pair<Float, Int>>) -> Unit = { _, _, _ -> },
) {
    val items by fractalManager.gradients.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(modifier = Modifier.padding(8.dp)) {

        items(items = items, key = { it.id }
        ) { gradient ->
            Row(
                modifier = Modifier.animateItemPlacement(
                    spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
                )
            ) {
                if (gradient.isDefault()) { // default gradients have negative ids
                    val offsetX = remember { Animatable(0f) }
                    GradientButtonWithEdit(
                        gradient,
                        fractalManager,
                        Modifier.shakeOnDrag(offsetX, coroutineScope),
                        onEdit
                    )
                } else {
                    DragToDelete(onDelete = { fractalManager.delete(gradient) }) {
                        GradientButtonWithEdit(gradient, fractalManager, onEdit = onEdit)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GradientButtonWithEdit(
    gradient: GradientData,
    fractalManager: FractalManager,
    modifier: Modifier = Modifier,
    onEdit: (Int, String, List<Pair<Float, Int>>) -> Unit,
) {
    var editButtonVisibility by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) { editButtonVisibility = true }
            .onPointerEvent(PointerEventType.Exit) { editButtonVisibility = false },
    ) {
        TextGradientButton(
            text = gradient.name,
            gradient = gradient.colorStops.map { it.first to Color(it.second) },
            onClick = { fractalManager.setGradient(gradient.colorStops) }
        )
        AppearanceAnimated(editButtonVisibility, Modifier.align(Alignment.CenterStart).offset(x = 8.dp)) {
            EditButton { onEdit(gradient.id, gradient.name, gradient.colorStops) }
        }
    }
}


@Composable
private fun ToolBar(fractalManager: FractalManager, switchToCreateMode: () -> Unit) {
    var showFileSaveDialog by remember { mutableStateOf(false) }
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
    ) {
        ToolBarIconButton(UndoIcon(), Localization.undo) { fractalManager.undo() }
        ToolBarIconButton(ResetIcon(), Localization.reset) { fractalManager.reset() }
        ToolBarIconButton(SaveIconOutlined(), Localization.save) { showFileSaveDialog = true }
        ToolBarIconButton(AddGradientIcon(), Localization.create) { switchToCreateMode() }
    }
    if (showFileSaveDialog) {
        ResizeImageSaveDialog(
            title = Localization.fileSaveOrResizeDialogTitle,
            currentImage = fractalManager.image.value,
            controller = getKoin().get(),
            selfClose = {
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