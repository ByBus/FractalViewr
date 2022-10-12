package data

import androidx.compose.runtime.mutableStateListOf

class GradientRepository(defaultGradients: DataSource<GradientData>) {
    var gradients = mutableStateListOf<GradientData>()
        private set

    init {
        gradients.addAll(defaultGradients.readAll())
    }

    fun save(gradientData: GradientData) {
        gradients.add(gradientData)
    }
}
