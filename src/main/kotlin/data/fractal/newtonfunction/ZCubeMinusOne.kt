package data.fractal.newtonfunction

import data.fractal.Complex
import kotlin.math.sqrt

class ZCubeMinusOne : NewtonFunctionBase() {
    override val roots = listOf(
        Complex(1.0, 0.0),
        Complex(-0.5, sqrt(3.0)/2.0),
        Complex(-0.5, -sqrt(3.0)/2.0),
    )

    override fun calculate(z: Complex): Complex {
        return z.cube() - 1.0
    }

    override fun derivative(z: Complex): Complex{
       return z.sqr() * 3.0
    }
}
