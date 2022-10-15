package domain

import java.io.File

interface FileSaver<T> {
    fun save(content: T, file: File)
}
