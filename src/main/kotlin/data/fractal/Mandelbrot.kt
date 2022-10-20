package data.fractal

import domain.Fractal

class Mandelbrot(private val maxIterations: Int = 255) : Fractal {
    override fun calculate(x0: Double, y0: Double): Int {
        val z = Complex(0.0, 0.0)
        val c = Complex(x0, y0)
        var iteration = 0
        while (z.abs() <= 4 && iteration < maxIterations) {
            z.sqr() + c
            iteration++
        }
        return iteration
    }
}