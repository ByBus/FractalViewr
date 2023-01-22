package domain.imageprocessing

import domain.Fractal

interface ConfigurationProvider<T> {
    fun provideState(stateConsumer: (fractal: Fractal, state: T) -> Unit)
}