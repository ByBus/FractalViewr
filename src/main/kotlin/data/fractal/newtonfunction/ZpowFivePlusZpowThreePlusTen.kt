package data.fractal.newtonfunction

import data.fractal.Complex

class ZpowFivePlusZpowThreePlusTen : NewtonFunctionBase() {
    override val roots: List<Complex> = listOf(
        Complex(real = 1.1770595176599319, img = -1.0),
        Complex(real = -0.44274236878052714, img = 1.6333276322769361),
        Complex(real = -0.44274236614506707, img = -1.6333276307214564),
        Complex(real = 1.1770594770838285, img = 1.0),
        Complex(real = -1.468634301989455, img = 0.0),
    )

    override fun calculate(z: Complex): Complex {
        return z.copy().pow(5.0) + z.pow(3.0) + 10.0
    }

    override fun derivative(z: Complex): Complex {
        return z.copy().pow(4.0) * 5.0 + z.sqr() * 3.0
    }
}