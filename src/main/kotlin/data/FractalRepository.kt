package data

import data.fractal.factory.FactoryMaker
import domain.Fractal
import domain.FractalFamilyRepository
import domain.FractalType
import kotlinx.coroutines.runBlocking

class FractalRepository(
    private val fractalDataSource: ReadSingleDataSource<FractalType, Fractal>,
    private val fractalFamilyFactoryMaker: FactoryMaker<FractalType>
) : FractalFamilyRepository {

    override fun familyName(type: FractalType): String {
        return fractalFamilyFactoryMaker.create(type).familyName
    }

    override fun familyFractalTypes(type: FractalType): Array<out FractalType> {
        return fractalFamilyFactoryMaker.create(type).types()
    }

    override fun getByType(type: FractalType): Fractal = runBlocking {
        fractalDataSource.readById(type)
    }
}