package data.fractal

import domain.Fractal

class Phoenix(
    private val maxIterations: Int = 255,
    private val c: Double = 0.5667,
    private val p: Double = -0.5,
) : Fractal {
    override fun calculate(x0: Double, y0: Double): Int {
        val zPrev = Complex(0.0, 0.0)
        val z = Complex(y0, x0)
        val c = Complex(p, c)
        val temp = Complex(z.real, z.img)
        var iteration = 0
        while (z.absSquared() <= 4 && iteration < maxIterations) {
            temp.set(real = z.real, img = z.img)
            z.sqr() + c.img + zPrev * c.real
            zPrev.set(temp.real, temp.img)
            iteration++
        }
        return iteration
    }
}