package data.fractal.factory

import domain.Fractal
import domain.FractalType

interface ConcreteFactory<T: FractalType> {
    val familyName: String
    fun types(): Array<out T>
    fun create(type: T): Fractal
}