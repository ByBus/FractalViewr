package domain

interface Configuration<T> : StateHolder<T> {
    val fractal: Fractal
    val canvasStateHistoryStack: StateHolder<T>
    val fractalFamilyState: FractalFamilyState
}

class FractalManagerConfiguration<T>(
    override val fractal: Fractal,
    override val canvasStateHistoryStack: StateHolder<T>,
    override val fractalFamilyState: FractalFamilyState,
) : Configuration<T>, StateHolder<T> by canvasStateHistoryStack