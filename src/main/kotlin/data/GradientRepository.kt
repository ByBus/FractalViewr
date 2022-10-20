package data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GradientRepository(defaultGradients: DataSource<GradientData>) {
    private var _gradients = MutableStateFlow(emptyList<GradientData>())
    val gradients = _gradients.asStateFlow()

    init {
        _gradients.value = defaultGradients.readAll()
    }

    fun save(gradientData: GradientData) {
        _gradients.value = listOf(gradientData) + _gradients.value
    }
}
