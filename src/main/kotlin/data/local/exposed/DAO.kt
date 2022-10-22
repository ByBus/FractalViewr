package data.local.exposed

interface DAO {
    suspend fun allGradients(): List<GradientDB>
    suspend fun gradient(id: Int): GradientDB?
    suspend fun addGradient(name: String, colors: List<Pair<Float, Int>>): GradientDB?
    suspend fun editGradient(id: Int, name: String, colors: List<Pair<Float, Int>>): Boolean
    suspend fun deleteGradient(id: Int): Boolean
}