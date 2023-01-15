package data.fractal.newtonfunction

import data.fractal.Complex

class ZCubeMinusTwoZPlusTwo : NewtonFunctionBase() {
    override val roots = listOf(
        Complex(-1.7692923122782263, 0.0),
        Complex(0.8846461771210563, -0.5897428050223634),
        Complex(0.8846461771210563, 0.5897428050223634),
    )

    override fun calculate(z: Complex): Complex {
        return z.copy().cube() - z.copy() * 2.0 + 2.0
    }

    override fun derivative(z: Complex): Complex {
        //3 * z^2 - 2
        return z.sqr() * 3.0 - 2.0
    }
}