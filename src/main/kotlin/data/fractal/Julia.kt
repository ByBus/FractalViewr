package data.fractal

import domain.Fractal

class Julia(private val maxIterations: Int = 255, private val realC: Double = -0.70176, private val imaginaryC: Double = -0.3842) : Fractal {

    override fun calculate(x0: Double, y0: Double): Int {
        var x = x0
        var y = y0
        var iteration = 0
        while (x * x + y * y <= 4 && iteration < maxIterations) {
            val temp = x * x - y * y + realC
            y = 2 * x * y + imaginaryC
            x = temp
            iteration++
        }
        return iteration
    }
}