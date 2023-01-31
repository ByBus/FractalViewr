package domain

class GradientData(val name: String, val colorStops: List<Pair<Float, Int>>, val id: Int = -1) {
    fun isDefault() = id < 0
}