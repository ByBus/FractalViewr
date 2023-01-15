package data.fractal

import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.cos as cosine
import kotlin.math.exp as exponent
import kotlin.math.sin as sinus

class Complex(var real: Double, var img: Double) {
    operator fun component1() = real
    operator fun component2() = img
    fun set(real: Double = real(), img: Double = imag()) {
        this.real = real
        this.img = img
    }

    fun real(): Double {
        return real
    }

    fun imag(): Double {
        return img
    }

    fun absSquared(): Double {
        return real * real + img * img
    }

    fun abs(): Double {
        return sqrt(absSquared())
    }

    operator fun plus(other: Complex): Complex {
        real += other.real()
        img += other.imag()
        return this
    }

    operator fun plus(other: Double): Complex {
        real += other
        return this
    }

    operator fun plus(other: Int): Complex {
        real += other.toDouble()
        return this
    }

    operator fun minus(other: Complex): Complex {
        real -= other.real()
        img -= other.imag()
        return this
    }

    operator fun minus(other: Double): Complex {
        real -= other
        return this
    }

    operator fun minus(other: Int): Complex {
        real -= other.toDouble()
        return this
    }

    operator fun times(other: Double): Complex {
        real *= other
        img *= other
        return this
    }

    operator fun times(other: Int): Complex {
        real *= other.toDouble()
        img *= other.toDouble()
        return this
    }

    operator fun times(other: Complex): Complex {
        val r = real * other.real() - img * other.imag()
        val i = img * other.real() + real * other.imag()
        set(r, i)
        return this
    }

    operator fun div(other: Complex): Complex {
        val tmp = other.absSquared()
        val r = (real * other.real() + img * other.imag()) / tmp
        val i = (img * other.real() - real * other.imag()) / tmp
        set(r, i)
        return this
    }

    fun sqr(): Complex {
        set(real * real - img * img, 2 * real * img)
        return this
    }

    fun cube(): Complex {
        val r = real * real
        val i = img * img
        set(real * (r - 3 * i), img * (3 * r - i))
        return this
    }

    fun exp(): Complex {
        val e1 = exponent(real)
        set(e1 * cosine(img), e1 * sinus(img))
        return this
    }

    fun sin(): Complex {
        val e1 = exponent(img)
        val e2 = 1 / e1
        set(sinus(real) * (e1 + e2) / 2, cosine(real) * (e1 - e2) / 2)
        return this
    }

    fun cos(): Complex {
        val e1 = exponent(img)
        val e2 = 1 / e1
        set(cosine(real) * (e1 + e2) / 2, -sinus(real) * (e1 - e2) / 2)
        return this
    }

    fun sqr(other: Complex): Complex {
        real = other.real() * other.real() - other.imag() * other.imag()
        img = 2 * other.real() * other.imag()
        return this
    }

    fun cube(other: Complex): Complex {
        val r = other.real() * other.real()
        val i = other.imag() * other.imag()
        set(other.real() * (r - 3 * i), other.imag() * (3 * r - i))
        return this
    }

    fun exp(other: Complex): Complex {
        val e = exponent(other.real())
        set(e * cosine(other.imag()), e * sinus(other.imag()))
        return this
    }

    fun sin(other: Complex): Complex {
        val e1 = exponent(other.imag())
        val e2 = 1 / e1
        set(sinus(other.real()) * (e1 + e2) / 2, cosine(other.real()) * (e1 - e2) / 2)
        return this
    }

    fun cos(other: Complex): Complex {
        val e1 = exponent(other.imag())
        val e2 = 1 / e1
        set(cosine(other.real()) * (e1 + e2) / 2, -sinus(other.real()) * (e1 - e2) / 2)
        return this
    }

    fun copy(r: Double = real, i: Double = img): Complex = Complex(r, i)

    fun pow(z1: Complex, z2: Double): Complex {
        val r = sqrt(z1.absSquared()).pow(z2)
        val theta: Double = z2 * z1.arg(this)
        return Complex(cosine(theta) * r, sinus(theta) * r)
    }

    private fun arg(z: Complex): Double {
        return atan2(z.img, z.real)
    }

    fun pow(n: Double): Complex {
        return pow(this, n)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Complex

        if (real != other.real) return false
        if (img != other.img) return false

        return true
    }

    override fun hashCode(): Int {
        var result = real.hashCode()
        result = 31 * result + img.hashCode()
        return result
    }
}