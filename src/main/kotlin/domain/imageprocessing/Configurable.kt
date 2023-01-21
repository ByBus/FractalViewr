package domain.imageprocessing

import domain.Fractal

interface Configurable<T> {
    fun setConfiguration(fractal: Fractal, state: T)
}