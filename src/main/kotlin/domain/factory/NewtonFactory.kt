package domain.factory

import Localization
import data.fractal.Newton
import data.fractal.newtonfunction.*
import domain.ConcreteFactory
import domain.Fractal
import domain.NewtonFamily

class NewtonFactory(override val familyName: String = Localization.newtonEquation) : ConcreteFactory<NewtonFamily> {
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