package data

class GradientRepository(defaultGradients: DataSource<GradientData>) {
    var gradients = mutableListOf<GradientData>()
        private set

    init {
        gradients.addAll(defaultGradients.readAll())
    }

}
