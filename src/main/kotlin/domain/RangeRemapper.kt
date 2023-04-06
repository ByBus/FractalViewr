package domain

interface RangeRemapper<T: Number, R: Number> {
    fun reMap(
        value: T,
        fromStart: T,
        fromEnd: T,
        toStart: R,
        toEnd: R
    ): R

    class IntDoubleReMapper: RangeRemapper<Int, Double> {
        override fun reMap(
            value: Int,
            fromStart: Int,
            fromEnd: Int,
            toStart: Double,
            toEnd: Double
        ): Double {
            return toStart + (value.toDouble() - fromStart) / (fromEnd - fromStart) * (toEnd - toStart)
        }
    }
}