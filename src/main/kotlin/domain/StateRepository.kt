package domain

interface StateRepository<T> {
    fun state(): T
    fun save(state: T)
    fun reset()
    fun removeLast()
}

interface StateRepositoryCreatable<T>: StateRepository<T> {
    fun createWithState(state: T): StateRepositoryCreatable<FractalSpaceState<Double>>
}