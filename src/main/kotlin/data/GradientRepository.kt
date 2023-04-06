package data

import domain.CrudRepository
import domain.GradientData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GradientRepository(
    private val defaultGradients: DataSource<GradientData>,
    private val persistedGradients: MutableDataSource<GradientData>,
) : CrudRepository<GradientData> {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var _gradients = MutableStateFlow(emptyList<GradientData>())
    override val gradients = _gradients.asStateFlow()

    init {
        runBlocking {
            refresh()
        }
    }

    override fun save(gradientData: GradientData) {
        executeAndRefresh { persistedGradients.save(gradientData) }
    }

    override fun delete(gradientData: GradientData) {
        executeAndRefresh { persistedGradients.delete(gradientData) }
    }

    override fun edit(gradientData: GradientData) {
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
