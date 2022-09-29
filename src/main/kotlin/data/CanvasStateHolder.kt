package data

import java.util.*

class CanvasStateHolder(private val initial: State) {
    //State(-1.5, 1.5, -2.0, 1.0)
    private val states = ArrayDeque<State>(100)

    class State(
        val yMin: Double, // Y_min
        val yMax: Double, // Y_max
        val xMin: Double, // X_min
        val xMax: Double  // X_max
    )

    fun state(): State {
        return with(states) {
            if (isEmpty()) initial else poll()
        }
    }

    fun save(state: State) {
        states.push(state)
    }

    fun clear() {
        states.clear()
    }
}