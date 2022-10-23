package data.local

import data.GradientData
import domain.MutableDataSource
import data.local.exposed.ColorDBToData
import data.local.exposed.DAO
import data.local.exposed.ExposedDB.dbQuery
import data.local.exposed.GradientDBToData

class ExposedGradients(private val database: DAO): MutableDataSource<GradientData> {
    override suspend fun delete(item: GradientData) {
        dbQuery {
            database.deleteGradient(item.id)
        }
    }

    override suspend fun update(item: GradientData) {
        dbQuery {
            with(item) {
                database.editGradient(id, name, colorStops)
            }
        }
    }

    override suspend fun save(item: GradientData) {
        return dbQuery {
            database.addGradient(item.name, item.colorStops)
                ?.map(mapper = GradientDBToData(colorMapper = ColorDBToData()))
        }
    }

    override suspend fun readAll(): List<GradientData> {
        return dbQuery {
            database.allGradients().map {
                it.map(mapper = GradientDBToData(colorMapper = ColorDBToData()))
            }
        }
    }
}