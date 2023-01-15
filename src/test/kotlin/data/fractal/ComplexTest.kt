package data.fractal

import org.junit.Assert.*
import org.junit.Test

class ComplexTest {

    @Test
    fun cube() {
        val complexNumber = Complex(3.0, -2.0)
        val actual = complexNumber.cube()
        val expected = Complex(-9.0, -46.0)
        assertEquals(expected, actual)
    }

    @Test
    fun sqr() {
        val complexNumber = Complex(3.0, -2.0)
        val actual = complexNumber.sqr()
        val expected = Complex(5.0, -12.0)
        assertEquals(expected, actual)
    }

    @Test
    fun devision1() {
        val complexNumber1 = Complex(3.0, 4.0)
        val complexNumber2 = Complex(8.0, -2.0)
        val actual = complexNumber1 / complexNumber2
        val expected = Complex(4.0 / 17.0, 19.0 / 34.0)
        assertEquals(expected, actual)
    }

    @Test
    fun devision2() {
        val complexNumber1 = Complex(3.0, 5.0)
        val complexNumber2 = Complex(-2.0, 2.0)
        val actual = complexNumber1 / complexNumber2
        val expected = Complex(0.5, -2.0)
        assertEquals(expected, actual)
    }
}