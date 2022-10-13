package domain

import data.CanvasState
import data.fractal.Mandelbrot

class FractalFactory(private val fractalManager: FractalManager) {
    fun changeConfiguration(type: FractalType) {
        val (fractal, state) = when (type) {
            FractalType.MANDELBROT -> Mandelbrot() to CanvasState(-2.0, 1.0, -1.5, 1.5)
        }
        fractalManager.setConfiguration(fractal, state)
    }
}

enum class FractalType {
    MANDELBROT
}
