package data.fractal

import domain.Fractal

class Lines(
    private val maxIterations: Int = 255,
    private val c: Double = 0.5667,
    private val p: Double = -0.5,
) : Fractal {
    override fun calculate(x0: Double, y0: Double): Int {
        var zPrev = Complex(0.0, 0.0)
        var z = Complex(y0, x0)
        val c = Complex(p, c)
        var iteration = 0
        while (z.absSquared() <= 4 && iteration < maxIterations) {
            val temp = Complex(z.img, z.real)
            z = z.sqr() + c.real + zPrev * c.img
            zPrev = temp
            iteration++
        }
        return iteration
    }
}