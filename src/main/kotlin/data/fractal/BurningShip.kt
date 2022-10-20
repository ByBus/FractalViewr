package data.fractal

import domain.Fractal
import kotlin.math.abs

class BurningShip(private val maxIterations: Int = 255) : Fractal {
    override fun calculate(x0: Double, y0: Double): Int {
        var z = Complex(x0, y0)
        val c = Complex(x0, y0)
        var iteration = 0
        while (z.abs() <= 4 && iteration < maxIterations) {
            z.sqr().set(img = abs(z.img))
            z += c
            iteration++
        }
        return iteration
    }
}