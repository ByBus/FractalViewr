package domain

data class Configuration(
    val fractal: Fractal,
    val canvasStateHolder: StateRepositoryCreatable<FractalSpaceState<Double>>
) : StateRepository<FractalSpaceState<Double>> {
    override fun state(): FractalSpaceState<Double> = canvasStateHolder.state()

    override fun reset() = canvasStateHolder.reset()

    override fun removeLast() = canvasStateHolder.removeLast()

    override fun save(state: FractalSpaceState<Double>) = canvasStateHolder.save(state)

    fun copyWith(fractal: Fractal, state: FractalSpaceState<Double>): Configuration {
        return copy(
            fractal = fractal,
            canvasStateHolder = canvasStateHolder.createWithState(state)
        )
    }
}