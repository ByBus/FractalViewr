package presenter

import org.junit.Assert.assertEquals
import org.junit.Test

class IntDoubleReMapperTest {

    val mapper = IntDoubleReMapper()

    @Test
    fun reMapWidth() {
        val actual = mapper.reMap(500, 0, 1000, 0.5, 1.0)
        val expected = 0.75
        assertEquals(expected, actual, 0.001)
    }

    @Test
    fun reMapHeight() {
        val actual = mapper.reMap(500, 0, 1000, 0.5, 1.0)
        val expected = 0.75
        assertEquals(expected, actual, 0.001)
    }

    @Test
    fun reMap0() {
        val actual = mapper.reMap(0, 0, 1000, 0.5, 1.0)
        val expected = 0.5
        assertEquals(expected, actual, 0.001)
    }

    @Test
    fun reMap1000() {
        val actual = mapper.reMap(1000, 0, 1000, 0.5, 1.0)
        val expected = 1.0
        assertEquals(expected, actual, 0.001)
    }

}