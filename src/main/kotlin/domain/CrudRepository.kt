package domain

import kotlinx.coroutines.flow.StateFlow

interface CrudRepository<T> {
    val gradients: StateFlow<List<T>>
    fun save(gradientData: T)
    fun delete(gradientData: T)
    fun edit(gradientData: T)
}