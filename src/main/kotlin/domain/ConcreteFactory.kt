package domain

interface ConcreteFactory<T: FractalType> {
    val familyName: String
    fun types(): Array<out T>
    fun create(type: T): Fractal
}