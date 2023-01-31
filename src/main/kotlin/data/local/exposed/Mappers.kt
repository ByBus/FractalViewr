package data.local.exposed

import domain.GradientData

interface GradientMapper<T> {
    operator fun invoke(id: Int, name: String, colors: Iterable<ColorDB>): T
}

interface ColorMapper<T>{
    operator fun invoke(position: Float, value: Int): T
}

class GradientDBToData(private val colorMapper: ColorMapper<Pair<Float, Int>>) : GradientMapper<GradientData> {
    override operator fun invoke(id: Int, name: String, colors: Iterable<ColorDB>): GradientData {
        return GradientData(
            name,
            colors.map { it.map(colorMapper) },
            id
        )
    }
}

class ColorDBToData : ColorMapper<Pair<Float, Int>> {
    override fun invoke(position: Float, value: Int) = position to value
}