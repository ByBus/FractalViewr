// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import data.CanvasState
import data.CanvasStateHolder
import domain.FractalManager
import data.GradientRepository
import data.fractal.Mandelbrot
import presenter.Palette
import presenter.ScreenMapper
import ui.AppIcon
import ui.TextGradientButton

private const val WIDTH = 1000
private const val HEIGHT = 1000

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App(fractalManager: FractalManager) {
    MaterialTheme {
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
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "37 / 142",
                        color = Color.Blue,
                        fontSize = 25.sp
                    )
                    Text(
                        text = "B2",
                        color = Color.Blue,
                        fontSize = 25.sp
                    )
                    Text(
                        text = "B3",
                        color = Color.Blue,
                        fontSize = 25.sp
                    )
                }
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(items = fractalManager.gradients) { gradient ->
                        TextGradientButton(
                            text = gradient.first,
                            gradient = gradient.second.map { Color(it) },
                            onClick = {
                                fractalManager.setGradient(gradient.second)
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
        Palette(),
        GradientRepository()
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(size = DpSize(1200.dp, Dp.Unspecified)),
        title = "FractalViewr",
        icon = AppIcon()
    ) {
        App(fractalManager)
    }
}
