package di


import data.*
import data.fractal.Mandelbrot
import data.local.ExposedGradients
import data.local.exposed.DAO
import data.local.exposed.ExposedDao
import domain.*
import data.fractal.factory.FactoryMaker
import data.fractal.factory.FractalFamilyFactoryMaker
import domain.FractalType
import data.palette.IntColorPalette
import data.palette.Interpolator
import domain.imageprocessing.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module
import ui.gradientmaker.ColorProducer
import ui.gradientmaker.controller.ColorPalette
import ui.gradientmaker.controller.ColorPickerController
import ui.gradientmaker.controller.CoordinateConverter
import ui.gradientmaker.controller.GradientSliderController
import java.awt.Color
import java.awt.image.BufferedImage

val mainModule = module(createdAtStart = true) {
    single {
        FractalManager(
            get(), get(), get(), get(),
            finalImageProcessor = get(named("final_image")),
            previewImageProcessor = get(named("preview_image")),
            configuration = get(named("initial_config"))
        )
    }.binds(arrayOf(FractalManager::class, ConfigurationProvider::class, Configurable::class))

    factory(named("initial_config")) {
        Configuration(
            Mandelbrot(),
            CanvasStateRepository(CanvasState(-2.0, 1.0, -1.5, 1.5))
        )
    }

    single<RangeRemapper<Int, Double>> { RangeRemapper.IntDoubleReMapper() }

    factory<Interpolator<Color>> { Interpolator.AwtColorInterpolator() }
    single<Palette<Int>> { IntColorPalette(255, get()) }

    singleOf(::DefaultGradients) { bind<DataSource<GradientData>>() }
    singleOf(::ExposedDao) { bind<DAO>() }
    singleOf(::ExposedGradients) { bind<MutableDataSource<GradientData>>() }
    singleOf(::GradientRepository) { bind<CrudRepository<GradientData>>() }

    factory<FileSaver<BufferedImage>> { ImageSaver() }

    singleOf(::Configurator)

    factoryOf(::FractalFamilyFactoryMaker) { bind<FactoryMaker<FractalType>>() }

    factory<FractalImageProcessor>(named("final_image")) {
        FinalImageProcessor(
            width = 1000,
            height = 1000,
            get(),
            get()
        )
    }

    factory<FractalImageProcessor>(named("preview_image")) {
        PreviewProcessor(
            width = 200,
            height = 200,
            get(),
            get()
        )
    }

    factory<FractalImageProcessor>(named("rescale_image_save_dialog")) { params ->
        RescaleImageProcessor(
            params.get(),
            params.get(),
            get(),
            get()
        )
    }

    factoryOf(::FileSaveDialogController)

    factoryOf(::FractalDatasource) { bind<ReadSingleDataSource<FractalType, Fractal>>() }
    factoryOf(::FractalRepository) { bind<FractalFamilyRepository>() }
}


val gradientMakerModule = module {
    factoryOf(::ColorPickerController)
    factoryOf(::CoordinateConverter)
    factory { GradientSliderController() }
    factoryOf(::ColorPalette) { bind<ColorProducer>() }
}