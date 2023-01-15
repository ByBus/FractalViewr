package data.fractal

import data.fractal.newtonfunction.Solution
import domain.Fractal

class Newton(
    private val maxIterations: Int = 255,
    private val function: NewtonFunction,
) : Fractal {
    override fun calculate(x0: Double, y0: Double): Int {
        val z = Complex(x0, y0)
        var iteration = 0
        var rootFractionAmongIterations = 1.0
        while (iteration < maxIterations) {
            z - function.calculate(z.copy()) / function.derivative(z.copy()) * function.coefficientA

            val solution = function.solution(z.copy())
            if (solution is Solution.Root) {
                val step = 1.0 / (solution.totalRoots * 2)
                rootFractionAmongIterations =
                    step * (1 + solution.rootIndex * 2) // for 3 roots and step = ___: 0.0|___r0___|___r1___|___r2___|1.0
                break
            }
            iteration++
        }
        return ((rootFractionAmongIterations * (maxIterations - 1)) / (1.0 + 0.01 * iteration)).toInt()
    }
}
