package di


import data.*
import data.local.ExposedGradients
import data.local.exposed.DAO
import data.local.exposed.ExposedDao
import domain.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import presenter.*
import ui.gradientmaker.ColorProducer
import ui.gradientmaker.controller.ColorPalette
import ui.gradientmaker.controller.ColorPickerController
import ui.gradientmaker.controller.CoordinateConverter
import ui.gradientmaker.controller.GradientSliderController
import java.awt.Color
import java.awt.image.BufferedImage

val mainModule = module(createdAtStart = true) {
    singleOf(::FractalManager)
    single<RangeRemapper<Int, Double>> { IntDoubleReMapper() }

    single<Interpolator<Color>> { AwtColorInterpolator() }
    single<Palette<Int>> { IntColorPalette(255, get()) }

    singleOf(::DefaultGradients) { bind<DataSource<GradientData>>() }
    singleOf(::ExposedDao) { bind<DAO>() }
    singleOf(::ExposedGradients) { bind<MutableDataSource<GradientData>>() }
    singleOf(::GradientRepository)

    single<FileSaver<BufferedImage>> { ImageSaver() }

    singleOf(::FractalFactory)
}


val gradientMakerModule = module {
    singleOf(::ColorPickerController)
    singleOf(::CoordinateConverter)
    single { GradientSliderController() }
    singleOf(::ColorPalette) { bind<ColorProducer>() }
}