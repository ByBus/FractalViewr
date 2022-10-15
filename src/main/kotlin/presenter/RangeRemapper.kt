package presenter

interface RangeRemapper<T: Number, R: Number> {
    fun reMap(
        value: T,
        fromStart: T,
        fromEnd: T,
        toStart: R,
        toEnd: R
    ): R
}