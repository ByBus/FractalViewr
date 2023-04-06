package data

import domain.FractalSpaceState
import domain.StateRepositoryCreatable
import java.util.*

class CanvasStateRepository (private var initialState: FractalSpaceState<Double>) :
    StateRepositoryCreatable<FractalSpaceState<Double>> {
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

    override fun createWithState(state: FractalSpaceState<Double>): StateRepositoryCreatable<FractalSpaceState<Double>> {
        return CanvasStateRepository(state)
    }
}