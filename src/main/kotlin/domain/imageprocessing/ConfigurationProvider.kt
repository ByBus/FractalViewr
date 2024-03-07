package domain.imageprocessing

import domain.Configuration

interface ConfigurationProvider<T> {
    fun provideConfig(configConsumer: (config: Configuration<T>) -> Unit)
}