package domain

import data.CanvasState
import data.fractal.Julia
import data.fractal.Mandelbrot

class FractalFactory(private val fractalManager: FractalManager) {
    fun changeConfiguration(type: FractalType) {
        val (fractal, state) = when (type) {
            MainFractals.JULIA, JuliaFamily.JULIA1 -> Julia() to CanvasState(-1.5, 1.5, -1.5, 1.5)
            JuliaFamily.JULIA2 -> Julia(realC = -0.835, imaginaryC = -0.2321) to CanvasState(-1.5, 1.5, -1.5, 1.5)
            JuliaFamily.JULIA3 -> Julia(realC = -0.8, imaginaryC = 0.156) to CanvasState(-1.5, 1.5, -1.5, 1.5)
            JuliaFamily.JULIA4 -> Julia(realC = 0.285, imaginaryC = 0.01) to CanvasState(-1.5, 1.5, -1.5, 1.5)
            JuliaFamily.JULIA5 -> Julia(realC = -0.7269, imaginaryC = 0.1889) to CanvasState(-1.5, 1.5, -1.5, 1.5)
            JuliaFamily.JULIA6 -> Julia(realC = -0.4, imaginaryC = 0.6) to CanvasState(-1.5, 1.5, -1.5, 1.5)
            JuliaFamily.JULIA7 -> Julia(realC = 0.0, imaginaryC =  -0.8) to CanvasState(-1.5, 1.5, -1.5, 1.5)
            else -> Mandelbrot() to CanvasState(-2.0, 1.0, -1.5, 1.5)
        }
        fractalManager.setConfiguration(fractal, state)
    }
}

interface FractalType {
    fun title() : String
}
enum class MainFractals(private val title: String) : FractalType {
    MANDELBROT("Mandelbrot"),
    JULIA("Julia");
    override fun title(): String = title
}

enum class JuliaFamily(private val title: String) : FractalType{
    JULIA1("-0.7-0.3i"),
    JULIA2("-0.8-0.2i"),
    JULIA3("-0.8+0.1i"),
    JULIA4("0.2+0.01i"),
    JULIA5("-0.7+0.2i"),
    JULIA6("-0.4+0.06i"),
    JULIA7("-0.8i");
    override fun title(): String = title
}

