package data.local.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Gradients : IntIdTable("gradients") {
    val name: Column<String> = varchar("name", 50)
}

class GradientDB(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GradientDB>(Gradients)

    var name by Gradients.name
    val colors by ColorDB referrersOn Colors.gradient

    fun <T> map(mapper: GradientMapper<T>): T {
        return mapper(id.value, name, colors)
    }
}

object Colors : IntIdTable("colors") {
    val position: Column<Float> = float("position")
    val value: Column<Int> = integer("value")
    val gradient = reference("color_to_gradient", Gradients)
}

class ColorDB(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ColorDB>(Colors)

    var position by Colors.position
    var value by Colors.value
    var gradient by GradientDB referencedOn Colors.gradient

    fun <T> map(mapper: ColorMapper<T>) : T {
        return mapper(position, value)
    }
}