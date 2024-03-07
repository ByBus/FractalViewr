package domain.imageprocessing

import domain.Configuration

interface Configurable<T> {
    fun setConfiguration(configuration: Configuration<T>)
}