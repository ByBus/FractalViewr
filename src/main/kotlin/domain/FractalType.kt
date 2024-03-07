package domain

interface FractalType {
    fun title(): String
    val hasFamilyOfFractals : Boolean
    fun familyTypes() : List<FractalType>
}

enum class MainFractals(private val title: String, family: Boolean = false) : FractalType {
    MANDELBROT("Mandelbrot"),
    JULIA("Julia", true),
    BURNING_SHIP("Burning Ship"),
    PHOENIX("Phoenix"),
    NEWTON("Newton", true),
    LINES("Lines");

    override fun title(): String = title

    override val hasFamilyOfFractals = family

    override fun familyTypes(): List<FractalType> = values().toList()
}

enum class JuliaFamily(
    private val realC: Double,
    private val imaginaryC: Double,
    private val prefix: String = "",
) : FractalType {
    JULIA1(-0.70176, -0.3842),
    JULIA2(-0.835, -0.2321),
    JULIA3(-0.8, 0.156),
    JULIA4(0.285, 0.01),
    JULIA5(-0.7269, 0.1889),
    JULIA6(-0.4, 0.6),
    JULIA7(0.0, -0.8),
    JULIA_CUBIC(0.53, 0.1, prefix = "Cubic");

    override fun title(): String {
        return String.format("$prefix %.2f%+.2fi", realC, imaginaryC)
    }

    fun <T> map(mapper: (Double, Double) -> T): T {
        return mapper(realC, imaginaryC)
    }

    override val hasFamilyOfFractals = false

    override fun familyTypes(): List<FractalType> = values().toList()
}

enum class NewtonFamily(
    private val title: String,
) : FractalType {
    NEWTON1("z^3 - 1"),
    NEWTON2("z^3 - 2z + 2"),
    NEWTON3("2z^4 + z^3 - 1"),
    NEWTON4("z^6 - 1"),
    NEWTON5("z^8 + 15z^4 - 16"),
    NEWTON6("z^5 + z^3 + 10");

    override fun title() = title
    override val hasFamilyOfFractals = false

    override fun familyTypes(): List<FractalType> = values().toList()
}