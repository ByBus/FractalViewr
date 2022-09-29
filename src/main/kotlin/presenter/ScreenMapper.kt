package presenter

class ScreenMapper(private var width: Int, private var height: Int) {
    fun reMapWidth(value: Int, rangeStart: Double, rangeEnd: Double): Double {
        return reMap(value, 0, width - 1, rangeStart, rangeEnd)
    }

    fun reMapHeight(value: Int, rangeStart: Double, rangeEnd: Double): Double {
        return reMap(value, 0, height - 1, rangeStart, rangeEnd)
    }

    fun setScreenSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    private fun reMap(
        value: Int,
        currentRangeStart: Int,
        currentRangeEnd: Int,
        newRangeStart: Double,
        newRangeEnd: Double
    ): Double {
        return newRangeStart + (value.toDouble() - currentRangeStart) / (currentRangeEnd - currentRangeStart) * (newRangeEnd - newRangeStart)
    }
}