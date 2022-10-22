package domain

interface FractalSpaceState<T> {
    fun scaledNear(direction: Float, x: T, y: T): FractalSpaceState<T>
    fun copy(): FractalSpaceState<T>
    fun shift(deltaX: T, deltaY: T)
    fun <R> mapAxis(mapper: (T, T) -> R, xAxis: Boolean = true): R
    fun <R> mapSize(mapper: (T, T) -> R): R
}