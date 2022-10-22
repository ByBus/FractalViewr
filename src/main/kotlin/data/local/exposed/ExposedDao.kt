package data.local.exposed

import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class ExposedDao : DAO {
    override suspend fun allGradients(): List<GradientDB> {
        return GradientDB.all().with(GradientDB::colors).sortedByDescending { it.id }
    }

    override suspend fun gradient(id: Int): GradientDB? {
        return GradientDB.findById(id)?.load(GradientDB::colors)
    }

    override suspend fun addGradient(name: String, colors: List<Pair<Float, Int>>): GradientDB {
        val newGradient = GradientDB.new { this.name = name }
        colors.forEach { createColor(it, newGradient) }
        return newGradient
    }

    override suspend fun editGradient(id: Int, name: String, colors: List<Pair<Float, Int>>): Boolean {
        val gradient = GradientDB.findById(id)
        gradient?.let { grad ->
            grad.name = name
            Colors.deleteWhere { Colors.gradient eq grad.id }
            colors.forEach { createColor(it, grad) }
        }
        return gradient != null
    }

    private fun createColor(colorStop: Pair<Float, Int>, grad: GradientDB) {
        ColorDB.new {
            position = colorStop.first
            value = colorStop.second
            gradient = grad
        }
    }

    override suspend fun deleteGradient(id: Int): Boolean {
        val gradient = GradientDB.findById(id)
        gradient?.let {
            it.delete()
            Colors.deleteWhere { Colors.gradient eq id }
            return true
        }
        return false
    }
}