package data.fractal.newtonfunction

import data.fractal.Complex

class ZpowSixMinusOne : NewtonFunctionBase() {
    override fun calculate(z: Complex): Complex {
        return z.copy().pow(6.0) - 1.0
    }

    override fun derivative(z: Complex): Complex {
        return z.copy().pow(5.0) * 6.0
    }

    override val roots: List<Complex> = listOf(
        Complex(real = -0.5, img = -0.8660253995955557),
        Complex(real = -0.5, img = 0.8660254044906253),
        Complex(real = 0.5, img = -0.8660254037708665),
        Complex(real = 1.0, img = 0.0),
        Complex(real = 0.5, img = 0.8660254037947783),
        Complex(real = -1.0, img = 0.0)    )

}