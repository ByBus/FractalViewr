package data.fractal.factory

import Localization
import data.fractal.Julia
import data.fractal.JuliaCubic
import domain.Fractal
import domain.FractalType
import domain.JuliaFamily

class JuliaFamilyFactory(override val familyName: String = Localization.juliaConstant) : ConcreteFactory<JuliaFamily> {
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

class JuliaFactory(private val juliaFamilyFactory: ConcreteFactory<JuliaFamily>) : ConcreteFactory<FractalType> {
    override val familyName: String
        get() = juliaFamilyFactory.familyName

    override fun types(): Array<out JuliaFamily> = juliaFamilyFactory.types()

    override fun create(type: FractalType): Fractal {
        return if (type is JuliaFamily) juliaFamilyFactory.create(type) else Julia()
    }
}
