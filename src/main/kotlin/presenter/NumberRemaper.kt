package presenter

interface NumberRemaper<T: Number, R: Number> {
    fun reMap(
        value: T,
        fromStart: T,
        fromEnd: T,
        toStart: R,
        toEnd: R
    ): R
}