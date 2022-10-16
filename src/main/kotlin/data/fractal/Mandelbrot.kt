package data.fractal

import domain.Fractal

class Mandelbrot(private val maxIterations: Int = 255) : Fractal {
    override fun calculate(x0: Double, y0: Double): Int {
        var x = 0.0
        var y = 0.0
        var iteration = 0
        while (x * x + y * y <= 4 && iteration < maxIterations) {
            val temp = x * x - y * y + x0
            y = 2 * x * y + y0
            x = temp
            iteration++
        }
        return iteration
    }
}