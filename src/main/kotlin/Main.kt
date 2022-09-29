// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.CanvasStateHolder
import data.FractalManager
import data.GradientRepository
import data.fractal.Mandelbrot
import presenter.Palette
import presenter.ScreenMapper
import ui.TextGradientButton

private const val WIDTH = 1000
private const val HEIGHT = 1000

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App(fractalManager: FractalManager) {
    MaterialTheme {
        Row(modifier = Modifier.fillMaxWidth()) {
            val canvasImg by remember { fractalManager.image }
            var number by remember { mutableStateOf(0f) }
            Column {
                Canvas(modifier = Modifier
                    .width(canvasImg.width.dp)
                    .height(canvasImg.height.dp)
                    .onPointerEvent(PointerEventType.Scroll) {
                        number += it.changes.first().scrollDelta.y

                        println("$number ${it.changes.first().position}")
                    }
                ) {
                    drawImage(image = canvasImg.toComposeImageBitmap())
                }
                SideEffect {
                    println("recomposition")
                    fractalManager.computePreview()
                }
            }
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                items(items = fractalManager.gradients) { gradient ->
                    TextGradientButton(
                        text = gradient.first,
                        gradient = gradient.second.map { Color(it) },
                        onClick = {
                            fractalManager.setGradient(gradient.second)
                            fractalManager.computeImage()
                        }
                    )
                }
            }
        }
    }
}

fun main() = application {
    val fractalManager = FractalManager(
        Mandelbrot(),
        ScreenMapper(500, 500),
        CanvasStateHolder(CanvasStateHolder.State(-1.5, 1.5, -2.0, 1.0)),
        Palette(),
        GradientRepository()
    )

    Window(onCloseRequest = ::exitApplication) {
        App(fractalManager)
    }
}
