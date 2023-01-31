package data

import domain.DataSource
import domain.GradientData

class DefaultGradients: DataSource<GradientData> {
    private var gradients = mutableListOf<GradientData>()
    init {
        with(gradients) {
            addAll(
                listOf(
                    "Gradient 1" to listOf(-16751718, -3529381, -16751718, -3878, -16751718, -3878, -16751718, -3529381, -3878, -16751718, -3529381, -3878),
                    "Gradient 2" to listOf(-63488, -8192, -16318720, -16712193, -16252673, -589569, -65536),
                    "Gradient 3" to listOf(-22784, -14087075, -2031591, -16752613),
                    "Gradient 4" to listOf(-14268845, -13984369, -1457046, -744863, -1609903, -14268845, -13984369, -1457046, -744863, -1609903),
                    "Gradient 5" to listOf(-1689274, -918802, -5711140, -12223587, -14863017, -1689274, -918802, -5711140, -12223587, -14863017),
                    "Gradient 6" to listOf(-10764053, -137396, -6568643, -744863, -362207),
                    "Gradient 7" to listOf(-12444145, -15136998, -16187089, -16513975, -16775324, -15979382, -15183183, -13009455, -7948827, -2888456, -923201, -472737, -22016, -3375104, -6727936, -9817085),
                ).mapIndexed { index, it ->
                    var position = 0f
                    val step = 1f / it.second.lastIndex
                    GradientData(it.first, it.second.map { colors ->
                        val pair = position.coerceAtMost(1f) to colors
                        position += step
                        pair
                    }, id = -(index + 1))
                }
            )
        }
    }

    override suspend fun readAll(): List<GradientData> = gradients.toList()
}