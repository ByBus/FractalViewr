package data.fractal

import domain.Fractal

class Julia(
    private val maxIterations: Int = 255,
    private val realC: Double = -0.70176,
    private val imaginaryC: Double = -0.3842,
) : Fractal {
    override fun calculate(x0: Double, y0: Double): Int {
        val z = Complex(x0, y0)
        val c = Complex(realC, imaginaryC)
        var iteration = 0
        while (z.abs() <= 4 && iteration < maxIterations) {
            z.sqr() + c
            iteration++
        }
        return iteration
    }
}