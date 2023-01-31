package domain

interface FractalFamilyRepository{
    fun getByType(type: FractalType): Fractal

    fun familyName(type: FractalType): String

    fun familyFractalTypes(type: FractalType): Array<out FractalType>
}