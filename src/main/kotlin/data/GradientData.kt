package data

import java.util.UUID

class GradientData(val name: String, val colorStops: List<Pair<Float, Int>>, val id: Long = UUID.randomUUID().mostSignificantBits, )