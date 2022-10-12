import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import data.*
import data.fractal.Mandelbrot
import domain.FractalManager
import presenter.Palette
import presenter.ScreenMapper
import ui.App
import ui.AppIcon
import ui.gradientmaker.controller.ColorPalette
import ui.gradientmaker.controller.ColorPickerController
import ui.gradientmaker.controller.CoordinateConverter
import ui.gradientmaker.controller.GradientSliderController

fun main() = application {
    val fractalManager = remember {
        FractalManager(
            Mandelbrot(),
            ScreenMapper(),
            CanvasStateHolder(CanvasState(-2.0, 1.0, -1.5, 1.5)),
            Palette(interpolator = AwtColorInterpolator()),
            GradientRepository(DefaultGradients())
        )
    }
    val colorPickerController = remember { ColorPickerController(CoordinateConverter(), colorPalette = ColorPalette())}
    val gradientSliderController = remember { GradientSliderController() }

    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(1280.dp, Dp.Unspecified),
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        title = "FractalViewr",
        icon = AppIcon()
    ) {
        App(fractalManager, colorPickerController, gradientSliderController)
    }
}
