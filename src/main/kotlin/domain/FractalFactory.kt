package domain

import data.CanvasState
import data.fractal.*

class FractalFactory(private val fractalManager: FractalManager) {
    fun changeConfiguration(type: FractalType) {
        val (fractal, state) = when (type) {
            MainFractals.JULIA -> Julia() to CanvasState(-1.5, 1.5, -1.5, 1.5)
            is JuliaFamily -> {
                val juliaFractal = type.map { r, i ->
                    if (type == JuliaFamily.JULIA_CUBIC)
                        JuliaCubic(realC = r, imaginaryC = i)
                    else
                        Julia(realC = r, imaginaryC = i)
                }
                juliaFractal to CanvasState(-1.5, 1.5, -1.5, 1.5)
            }

            MainFractals.BURNING_SHIP -> BurningShip() to CanvasState(-2.2, 1.3, -2.0, 1.0)
            MainFractals.PHOENIX -> Phoenix() to CanvasState(-1.5, 1.5, -1.5, 1.5)
            MainFractals.LINES -> Lines() to CanvasState(-1.5, 1.5, -1.5, 1.5)
            else -> Mandelbrot() to CanvasState(-2.0, 1.0, -1.5, 1.5)
        }
        fractalManager.setConfiguration(fractal, state)
    }
}

interface FractalType {
    fun title(): String
}

enum class MainFractals(private val title: String) : FractalType {
    MANDELBROT("Mandelbrot"),
    JULIA("Julia"),
    BURNING_SHIP("Burning Ship"),
    PHOENIX("Phoenix"),
    LINES("Lines");

    override fun title(): String = title
}

enum class JuliaFamily(
    private val realC: Double,
    private val imaginaryC: Double,
    private val prefix: String = "",
) : FractalType {
    JULIA1(-0.70176, -0.3842),
    JULIA2(-0.835, -0.2321),
    JULIA3(-0.8, 0.156),
    JULIA4(0.285, 0.01),
    JULIA5(-0.7269, 0.1889),
    JULIA6(-0.4, 0.6),
    JULIA7(0.0, -0.8),
    JULIA_CUBIC(0.53, 0.1, prefix = "Cubic");

    override fun title(): String {
        return String.format("$prefix %.2f%+.2fi", realC, imaginaryC)
    }

    fun <T> map(mapper: (Double, Double) -> T): T {
        return mapper(realC, imaginaryC)
    }
}

