package data

interface MutableDataSource<T>: Save<T>, Read<T>

interface DataSource<T>: Read<T>

interface Save<T> {
    fun save(item: T)
}

interface Read<T> {
    fun readAll() : List<T>
}
