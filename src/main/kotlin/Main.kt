import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import data.*
import data.fractal.Mandelbrot
import domain.FractalManager
import presenter.Palette
import presenter.ScreenMapper
import ui.*
import ui.style.FractalTheme

private const val WIDTH = 1000
private const val HEIGHT = 1000

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App(fractalManager: FractalManager) {
    FractalTheme {
        Row(modifier = Modifier) {
            Column {
                val canvasImg = fractalManager.image
                var invalidate by remember { mutableStateOf(0) }
                var dragInitPosition = remember { Offset(0f, 0f) }
                Canvas(modifier = Modifier
                    .width(canvasImg.width.dp)
                    .height(canvasImg.height.dp)
                    .onPointerEvent(PointerEventType.Scroll) {
                        with(it.changes.first()) {
                            invalidate--
                            fractalManager.setScroll(scrollDelta.y, position.x.toInt(), position.y.toInt())
                            fractalManager.computePreview()
                            fractalManager.computeImage()
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                dragInitPosition = it
                                fractalManager.saveCurrent()
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
            Column(modifier = Modifier) {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier) {
                    BottomNavigationItem(
                        icon = { Icon(painter = UndoIcon(), contentDescription = null, modifier = Modifier.size(32.dp)) },
                        label = {Text("Undo",  maxLines = 1,
                            overflow = TextOverflow.Ellipsis)},
                        selected = false,
                        onClick = {}
                    )
                    BottomNavigationItem(
                        icon = { Icon(painter = SaveIconOutlined(), contentDescription = null, modifier = Modifier.size(32.dp)) },
                        label = {Text("Save",  maxLines = 1,
                            overflow = TextOverflow.Ellipsis)},
                        selected = false,
                        onClick = {}
                    )
                    BottomNavigationItem(
                        icon = { Icon(painter = AddGradientIcon(), contentDescription = null, modifier = Modifier.size(32.dp)) },
                        label = {Text("Create",  maxLines = 1,
                            overflow = TextOverflow.Ellipsis)},
                        selected = false,
                        onClick = {}
                    )
                }
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(items = fractalManager.gradients) { gradient ->
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
    }
}



fun main() = application {
    val fractalManager = FractalManager(
        Mandelbrot(),
        ScreenMapper(),
        CanvasStateHolder(CanvasState(-2.0, 1.0, -1.5, 1.5)),
        Palette(interpolator = AwtColorInterpolator()),
        GradientRepository(DefaultGradients())
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(1280.dp, Dp.Unspecified),
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        title = "FractalViewr",
        icon = AppIcon()
    ) {
        App(fractalManager)
    }
}
