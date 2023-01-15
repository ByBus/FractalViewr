package data.fractal.newtonfunction

import data.fractal.Complex

class ZpowEightPlusFifteenZpowFourMinusSixteen : NewtonFunctionBase() {
    override fun calculate(z: Complex): Complex {
        return z.copy().pow(8.0) + z.pow(4.0) * 15.0 - 16.0
    }

    override fun derivative(z: Complex): Complex {
        return z.copy().pow(7.0) * 8.0 + z.cube() * 30.0
    }

    override val roots: List<Complex> = listOf(
        Complex(real = -1.4142140808335364, img = -1.414214080833536),
        Complex(real = -1.414214627960515, img = 1.4142128847421676),
        Complex(real = 0.0, img = 1.0),
        Complex(real = 0.0, img = -1.0),
        Complex(real = -1.0, img = 0.0),
        Complex(real = 1.4142125286692275, img = 1.414212670904958),
        Complex(real = 1.0, img = 0.0),
        Complex(real = 1.4142128664511047, img = -1.4142123723446662),
    )

}