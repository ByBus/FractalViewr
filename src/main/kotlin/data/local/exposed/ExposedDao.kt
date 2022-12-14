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
        if (gradient == null) {
            addGradient(name, colors)
        } else {
            gradient.name = name
            Colors.deleteWhere { Colors.gradient eq gradient.id }
            colors.forEach { createColor(it, gradient) }
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
        Colors.deleteWhere { gradient eq id }
        Gradients.deleteWhere { Gradients.id eq id }
        return true
    }
}