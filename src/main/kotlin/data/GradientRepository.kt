package data

import domain.DataSource
import domain.MutableDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GradientRepository(
    private val defaultGradients: DataSource<GradientData>,
    private val persistedGradients: MutableDataSource<GradientData>,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var _gradients = MutableStateFlow(emptyList<GradientData>())
    val gradients = _gradients.asStateFlow()

    init {
        runBlocking {
            refresh()
        }
    }

    fun save(gradientData: GradientData) {
        executeAndRefresh { persistedGradients.save(gradientData) }
    }

    fun delete(gradientData: GradientData) {
        executeAndRefresh { persistedGradients.delete(gradientData) }
    }

    fun edit(gradientData: GradientData) {
        executeAndRefresh { persistedGradients.update(gradientData) }
    }

    private suspend fun refresh() {
        _gradients.value = persistedGradients.readAll() + defaultGradients.readAll()
    }

    private fun executeAndRefresh(action: suspend () -> Unit) {
        coroutineScope.launch {
            action()
            refresh()
        }
    }
}
