package data.fractal.newtonfunction

import data.fractal.Complex
import data.fractal.NewtonFunction
import kotlin.math.abs

abstract class NewtonFunctionBase : NewtonFunction {
    private val tolerance = 0.000001
    protected abstract val roots: List<Complex>
    override val coefficientA: Double = 1.0

    override fun solution(z: Complex): Solution {
        var solution: Solution = Solution.NoSolution
        for ((i, root) in roots.withIndex()) {
            val (real, img) = z.copy() - root
            if (abs(real) < tolerance && abs(img) < tolerance) {
                solution = Solution.Root(root, i, roots.size)
                break
            }
        }
        return solution
    }
}

sealed class Solution {
    object NoSolution : Solution()
    class Root(val root: Complex, val rootIndex: Int, val totalRoots: Int) : Solution()
}
