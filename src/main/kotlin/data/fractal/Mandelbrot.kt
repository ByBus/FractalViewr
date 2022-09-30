package data.fractal

private const val MAX_ITERATIONS = 255

class Mandelbrot : Fractal {

    override fun calculate(x0: Double, y0: Double): Int {
        var x = 0.0
        var y = 0.0
        var iteration = 0
        while (x * x + y * y <= 4 && iteration < MAX_ITERATIONS) {
            val temp = x * x - y * y + x0
            y = 2 * x * y + y0
            x = temp
            iteration++
        }
        return iteration
    }
}