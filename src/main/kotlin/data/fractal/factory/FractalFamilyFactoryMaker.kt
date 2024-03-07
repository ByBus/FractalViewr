package data.fractal.factory

import domain.FractalType
import domain.MainFractals
import domain.NewtonFamily

class FractalFamilyFactoryMaker : FactoryMaker<FractalType> {
    override fun defaultFactory() = JuliaFamilyFactory()
    override fun create(type: FractalType): ConcreteFactory<FractalType> {
        return when (type) {
            MainFractals.NEWTON, is NewtonFamily -> NewtonFactory(NewtonFamilyFactory())
            else -> JuliaFactory(defaultFactory())
        }
    }
}


interface FactoryMaker<T: FractalType> {
    fun defaultFactory() : ConcreteFactory<out T>

    fun create(type: T): ConcreteFactory<T>
}