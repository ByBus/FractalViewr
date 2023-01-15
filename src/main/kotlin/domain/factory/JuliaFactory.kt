package domain.factory

import Localization
import data.fractal.Julia
import data.fractal.JuliaCubic
import domain.ConcreteFactory
import domain.Fractal
import domain.JuliaFamily

class JuliaFactory(override val familyName: String = Localization.juliaConstant) : ConcreteFactory<JuliaFamily> {
    override fun create(type: JuliaFamily): Fractal {
        return type.map { r, i ->
            if (type == JuliaFamily.JULIA_CUBIC)
                JuliaCubic(realC = r, imaginaryC = i)
            else
                Julia(realC = r, imaginaryC = i)
        }
    }

    override fun types(): Array<JuliaFamily> {
        return JuliaFamily.values()
    }
}