package di


import data.*
import domain.FileSaver
import domain.FractalFactory
import domain.FractalManager
import domain.ImageSaver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presenter.Palette
import presenter.RangeRemapper
import presenter.ScreenMapper
import ui.gradientmaker.ColorProducer
import ui.gradientmaker.controller.ColorPalette
import ui.gradientmaker.controller.ColorPickerController
import ui.gradientmaker.controller.CoordinateConverter
import ui.gradientmaker.controller.GradientSliderController
import java.awt.Color
import java.awt.image.BufferedImage

val mainModule = module(createdAtStart = true) {
    singleOf(::FractalManager)
    single<RangeRemapper<Int, Double>> { ScreenMapper() }

    single<Interpolator<Color>> { AwtColorInterpolator() }
    single { Palette(255, get()) }

    single<DataSource<GradientData>> { DefaultGradients() }
    singleOf(::GradientRepository)

    single<FileSaver<BufferedImage>> { ImageSaver() }

    singleOf(::FractalFactory)
}

val colorPickerModule = module {
    singleOf(::ColorPickerController)
    singleOf(::CoordinateConverter)
    single { GradientSliderController() }
    single<ColorProducer> { ColorPalette() }
}