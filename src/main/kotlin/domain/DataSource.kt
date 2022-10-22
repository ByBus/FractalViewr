package domain

interface DataSource<T>: Read<T>

interface MutableDataSource<T>: Save<T>, Read<T>

interface Save<T> {
    suspend fun delete(item: T)
    suspend fun edit(item: T)
    suspend fun save(item: T)
}
interface Read<T> {
    suspend fun readAll() : List<T>
}
