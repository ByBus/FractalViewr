package data.fractal.factory

import Localization
import domain.FractalType
import data.NewtonFamily
import data.fractal.Newton
import data.fractal.newtonfunction.*
import domain.*

class NewtonFamilyFactory(override val familyName: String = Localization.newtonEquation) :
    ConcreteFactory<NewtonFamily> {
    override fun create(type: NewtonFamily): Fractal {
        val equation = when (type) {
            NewtonFamily.NEWTON1 -> ZCubeMinusOne()
            NewtonFamily.NEWTON2 -> ZCubeMinusTwoZPlusTwo()
            NewtonFamily.NEWTON3 -> TwoZpowFourPlusZpowThreeMinusOne()
            NewtonFamily.NEWTON4 -> ZpowSixMinusOne()
            NewtonFamily.NEWTON5 -> ZpowEightPlusFifteenZpowFourMinusSixteen()
            NewtonFamily.NEWTON6 -> ZpowFivePlusZpowThreePlusTen()
        }
        return Newton(function = equation)
    }

    override fun types(): Array<NewtonFamily> {
        return NewtonFamily.values()
    }
}

class NewtonFactory(private val newtonFamilyFactory: ConcreteFactory<NewtonFamily>) : ConcreteFactory<FractalType> {
    override val familyName: String
        get() = newtonFamilyFactory.familyName

    override fun types(): Array<out NewtonFamily> = newtonFamilyFactory.types()

    override fun create(type: FractalType): Fractal {
        return if (type is NewtonFamily) newtonFamilyFactory.create(type) else create(NewtonFamily.NEWTON1)
    }
}
