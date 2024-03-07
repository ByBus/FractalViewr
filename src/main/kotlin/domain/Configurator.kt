package domain

import domain.imageprocessing.Configurable
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent

interface Configurator {
    fun changeConfiguration(type: FractalType)
    val initialType: FractalType

    class Base(
        private val fractalManager: Configurable<FractalSpaceState<Double>>,
        private val fractalRepository: FractalFamilyRepository,
    ) : Configurator {
        override fun changeConfiguration(type: FractalType) {
            val fractal = fractalRepository.getByType(type)
            val state = when (type) {
                is JuliaFamily, MainFractals.JULIA -> CanvasState(-1.5, 1.5, -1.5, 1.5)
                is NewtonFamily, MainFractals.NEWTON -> CanvasState(-2.0, 2.0, -2.0, 2.0)
                MainFractals.BURNING_SHIP -> CanvasState(-2.2, 1.3, -2.0, 1.0)
                MainFractals.PHOENIX -> CanvasState(-1.5, 1.5, -1.5, 1.5)
                MainFractals.LINES -> CanvasState(-1.5, 1.5, -1.5, 1.5)
                else -> CanvasState(-2.0, 1.0, -1.5, 1.5)
            }
            val fractalTypes = fractalRepository.familyFractalTypes(type).toList()
            val familyName = fractalRepository.familyName(type)

            fractalManager.setConfiguration(KoinJavaComponent.getKoin().get { parametersOf(fractal, state, fractalTypes, familyName) })
        }

        override val initialType: FractalType = MainFractals.MANDELBROT
    }
}