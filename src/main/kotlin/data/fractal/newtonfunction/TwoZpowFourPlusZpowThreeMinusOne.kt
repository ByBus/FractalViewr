package data.fractal.newtonfunction

import data.fractal.Complex

class TwoZpowFourPlusZpowThreeMinusOne : NewtonFunctionBase() {
    override val roots: List<Complex> = listOf(
        Complex(real = 0.7389836162768431, img = 0.0),
        Complex(real = -1.0, img = 0.0),
        Complex(real = -0.11949181094906476, img = 0.8138345595089559),
        Complex(real = -0.11949181073266012, img = -0.813834558902373),
    )

    override fun calculate(z: Complex): Complex {
        return z.copy().pow(4.0) * 2.0 + z.pow(3.0) - 1.0
    }

    override fun derivative(z: Complex): Complex {
        return z.copy().pow(3.0) * 8.0 + z.sqr() * 3.0
    }
}