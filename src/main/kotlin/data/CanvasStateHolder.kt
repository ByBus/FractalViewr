package data

import domain.FractalSpaceState
import java.util.*

class CanvasStateHolder (private var initialState: FractalSpaceState<Double>) {
    private val states = ArrayDeque<FractalSpaceState<Double>>(100)

    fun state(): FractalSpaceState<Double> {
        return with(states) {
            if (isEmpty()) initialState else peek()
        }
    }

    fun save(state: FractalSpaceState<Double>) {
        states.push(state)
    }
    fun reset() {
        save(initialState)
    }

    fun removeLast() {
        states.poll()
    }
}