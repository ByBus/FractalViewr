package data.fractal

import domain.Fractal

class JuliaCubic(
    private val maxIterations: Int = 255,
    private val realC: Double = 0.53,
    private val imaginaryC: Double = 0.1,
) : Fractal {
    override fun calculate(x0: Double, y0: Double): Int {
        val z = Complex(x0, y0)
        val c = Complex(realC, imaginaryC)
        var iteration = 0
        while (z.absSquared() <= 4 && iteration < maxIterations) {
            z.cube() + c
            iteration++
        }
        return iteration
    }
}