package data

import data.fractal.BurningShip
import data.fractal.Lines
import data.fractal.Mandelbrot
import data.fractal.Phoenix
import domain.*
import data.fractal.factory.FactoryMaker
import domain.FractalType

class FractalDatasource(
    private val fractalFamilyFactoryMaker: FactoryMaker<FractalType>,
) : ReadSingleDataSource<FractalType, Fractal> {

    override suspend fun readById(id: FractalType): Fractal {
        return when(id){
            MainFractals.MANDELBROT -> Mandelbrot()
            MainFractals.BURNING_SHIP -> BurningShip()
            MainFractals.PHOENIX -> Phoenix()
            MainFractals.LINES -> Lines()
            else -> fractalFamilyFactoryMaker.create(id).create(id)
        }
    }
}