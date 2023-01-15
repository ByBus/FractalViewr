package domain.factory

import domain.ConcreteFactory
import domain.FractalType
import domain.MainFractals

class FractalFamilyFactoryMaker {
    fun create(type: FractalType): ConcreteFactory<*> {
        return when (type) {
            MainFractals.NEWTON -> NewtonFactory()
            else -> JuliaFactory()
        }
    }
}