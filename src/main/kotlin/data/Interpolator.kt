package data

interface Interpolator<T> {
    fun interpolate(from: T, to: T, fraction: Double): T
}