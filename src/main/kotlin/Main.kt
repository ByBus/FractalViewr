import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import data.AwtColorInterpolator
import data.DefaultGradients
import data.GradientRepository
import domain.FractalFactory
import domain.FractalManager
import domain.ImageSaver
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
            ScreenMapper(),
            Palette(interpolator = AwtColorInterpolator()),
            GradientRepository(DefaultGradients()),
            ImageSaver()
        )
    }
    val fractalFactory = remember { FractalFactory(fractalManager) }
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
