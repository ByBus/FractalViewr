package domain

import java.util.*

class CanvasStateStack (private var initialState: FractalSpaceState<Double>) :
    StateHolder<FractalSpaceState<Double>> {
    private val states = ArrayDeque<FractalSpaceState<Double>>(100)

    override fun state(): FractalSpaceState<Double> {
        return with(states) {
            if (isEmpty()) initialState else peek()
        }
    }

    override fun save(state: FractalSpaceState<Double>) {
        states.push(state)
    }

    override fun reset() {
        save(initialState)
    }

    override fun removeLast() {
        states.poll()
    }
}