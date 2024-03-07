package data.fractal.newtonfunction

import data.fractal.Complex
import data.fractal.NewtonFunction
import org.junit.Test
import domain.RangeRemapper
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.test.assertNotEquals

class FindNewtonFunctionsRoots {
    private val rootsHeader = "-${"=".repeat(25)}ROOTS${"=".repeat(25)}-"
    private val TOLERANCE = 10.0.pow(-6.0)
    private val remapper = RangeRemapper.IntDoubleReMapper()

    @Test
    fun `find ZCubeMinus2ZPlus2 Roots`(){
        val equation = ZCubeMinusTwoZPlusTwo()
        val roots = findRoots(equation)
        prettyPrint(roots)
        assertNotEquals(emptyList(), roots)
    }

    @Test
    fun `find ZCubeMinusOne Roots`(){
        val equation = ZCubeMinusOne()
        val roots = findRoots(equation)
        prettyPrint(roots)
        assertNotEquals(emptyList(), roots)
    }

    @Test
    fun `find Zpow8plus15Zpow4minus16 Roots`(){
        val equation = ZpowEightPlusFifteenZpowFourMinusSixteen()
        val roots = findRoots(equation)
        prettyPrint(roots)
        assertNotEquals(emptyList(), roots)
    }

    @Test
    fun `find Zpow6minus1 Roots`(){
        val equation = ZpowSixMinusOne()
        val roots = findRoots(equation)
        prettyPrint(roots)
        assertNotEquals(emptyList(), roots)
    }

    @Test
    fun `find SinZminus1 Roots`(){
        val equation = SinZminus1()
        val roots = findRoots(equation)
        prettyPrint(roots)
        assertNotEquals(emptyList(), roots)
    }

    @Test
    fun `find TwoZpow4plusZpow3minus1 Roots`(){
        val equation = TwoZpowFourPlusZpowThreeMinusOne()
        val roots = findRoots(equation)
        prettyPrint(roots)
        assertNotEquals(emptyList(), roots)
    }

    @Test
    fun `find ZpowFivePlusZpowThreePlusTen Roots`(){
        val equation = ZpowFivePlusZpowThreePlusTen()
        val roots = findRoots(equation)
        prettyPrint(roots)
        assertNotEquals(emptyList(), roots)
    }

    @Test
    fun `find ZCubeMinusOneWithAEqualsTwo Roots`(){
        val equation = ZCubeMinusOneWithAEqualsTwo()
        val roots = findRoots(equation)
        prettyPrint(roots)
        assertNotEquals(emptyList(), roots)
    }

    private fun findRoots(equation: NewtonFunction): List<Complex> {
        val rootsFound = mutableListOf<Complex>()
        (0..1000).forEach { x ->
            (0..1000).forEach { y ->
                val xDouble = remapper.reMap(x, 0, 1000, -2.0, 2.0)
                val yDouble = remapper.reMap(y, 0, 1000, -2.0, 2.0)
                val root = findRoot(Complex(xDouble, yDouble), equation)
                root?.let { z ->
                    rootsFound.addIfNewRoot(z)
                }
            }
        }
        return rootsFound
    }

    private fun prettyPrint(roots: List<Complex>) {
        println(rootsHeader)
        roots
            .map { "Complex(real = ${it.real}, img = ${it.img})," }
            .forEach { println(it) }
        println(rootsHeader)
    }

    private fun findRoot(start: Complex, equation: NewtonFunction): Complex? {
        var z = start
        for (i in 0..255) {
            val z1 = z.copy() - ((equation.calculate(z.copy()) / equation.derivative(z.copy())) * equation.coefficientA)
            if (distance(z, z1) <= TOLERANCE) { // convergence to root
                return z
            }
            z = z1
        }
        return null
    }

    private fun MutableList<Complex>.addIfNewRoot(z: Complex) {
        if (this.none { distance(it, z) <= 0.001 }) this.add(z)
    }

    private fun distance(z1: Complex, z2: Complex) : Double {
        return sqrt((z1.real - z2.real).pow(2.0) + (z1.img - z2.img).pow(2.0))
    }
}