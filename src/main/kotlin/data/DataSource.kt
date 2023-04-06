package data

interface DataSource<T>: ReadAll<T>

interface MutableDataSource<T>: Save<T>, ReadAll<T>

interface Save<T> {
    suspend fun delete(item: T)
    suspend fun update(item: T)
    suspend fun save(item: T)
}
interface ReadAll<T> {
    suspend fun readAll() : List<T>
}

interface ReadSingleDataSource<ID, T> {
    suspend fun readById(id: ID) : T
}
