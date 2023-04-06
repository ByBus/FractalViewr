package domain

interface Palette<T> {
    fun color(position: Int): T
    fun setColors(gradient: List<Pair<Float, T>>)
}