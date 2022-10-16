package presenter

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