package domain

interface StateHolder<T> {
    fun state(): T
    fun save(state: T)
    fun reset()
    fun removeLast()
}