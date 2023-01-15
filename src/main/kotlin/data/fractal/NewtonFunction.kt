package data.fractal

import data.fractal.newtonfunction.Solution

interface NewtonFunction {
    fun calculate(z: Complex): Complex
    fun derivative(z: Complex): Complex
    fun solution(z: Complex): Solution
    val coefficientA: Double
}