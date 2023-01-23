import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import di.gradientMakerModule
import di.mainModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.App
import ui.AppIcon

fun main() {
    startKoin {
        modules(gradientMakerModule, mainModule)
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(
                size = DpSize(1280.dp, Dp.Unspecified),
                position = WindowPosition.Aligned(Alignment.Center)
            ),
            title = Localization.appName,
            icon = AppIcon()
        ) {
            App(
                fractalManager = getKoin().get(),
                configurator = getKoin().get(),
            )
        }
    }
}
