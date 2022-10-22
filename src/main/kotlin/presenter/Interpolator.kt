package presenter

interface Interpolator<T> {
    fun interpolate(from: T, to: T, fraction: Double): T
}