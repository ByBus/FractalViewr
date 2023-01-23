package domain

import java.io.File

interface FileSaver<T> {
    suspend fun save(content: T, file: File)

    fun progressProvider(progressConsumer: (Float) -> Unit)
}
