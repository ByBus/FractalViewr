package data

import java.util.*

class CanvasStateHolder(private var initialState: CanvasState) {
    //State( -2.0, 1.0, -1.5, 1.5)
    private val states = ArrayDeque<CanvasState>(100)

    fun state(): CanvasState {
        return with(states) {
            if (isEmpty()) initialState else peek()
        }
    }

    fun save(state: CanvasState) {
        states.push(state)
    }

    fun clear() {
        states.clear()
    }
}