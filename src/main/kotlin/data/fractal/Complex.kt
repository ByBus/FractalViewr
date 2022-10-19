package data.fractal

import kotlin.math.cos as cosine
import kotlin.math.exp as exponent
import kotlin.math.sin as sinus

internal class Complex(var real: Double, var img: Double) {
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

    fun abs(): Double {
        return real * real + img * img
    }

    operator fun plus(p: Complex): Complex {
        real += p.real()
        img += p.imag()
        return this
    }

    operator fun plus(p: Double): Complex {
        real += p
        return this
    }

    operator fun plus(p: Int): Complex {
        real += p.toDouble()
        return this
    }

    operator fun minus(p: Complex): Complex {
        real -= p.real()
        img -= p.imag()
        return this
    }

    operator fun minus(p: Double): Complex {
        real -= p
        return this
    }

    operator fun minus(p: Int): Complex {
        real -= p.toDouble()
        return this
    }

    operator fun times(p: Double): Complex {
        real *= p
        img *= p
        return this
    }

    operator fun times(p: Int): Complex {
        real *= p.toDouble()
        img *= p.toDouble()
        return this
    }

    operator fun times(p: Complex): Complex {
        real = real * p.real() - img * p.imag()
        img = img * p.real() + real * p.imag()
        return this
    }

    operator fun div(p: Complex): Complex {
        val tmp = p.abs()
        real = (real * p.real() + img * p.imag()) / tmp
        img = (img * p.real() - real * p.imag()) / tmp
        return this
    }

    fun sqr(): Complex {
        set(real * real - img * img, 2 * real * img)
        return this
    }

    fun cube(): Complex {
        val r2 = real * real
        val i2 = img * img
        set(real * (r2 - 3 * i2), img * (3 * r2 - i2))
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

    fun sqr(p: Complex): Complex {
        real = p.real() * p.real() - p.imag() * p.imag()
        img = 2 * p.real() * p.imag()
        return this
    }

    fun cube(p: Complex): Complex {
        val r2 = p.real() * p.real()
        val i2 = p.imag() * p.imag()
        set(p.real() * (r2 - 3 * i2), p.imag() * (3 * r2 - i2))
        return this
    }

    fun exp(p: Complex): Complex {
        val e1 = exponent(p.real())
        set(e1 * cosine(p.imag()), e1 * sinus(p.imag()))
        return this
    }

    fun sin(p: Complex): Complex {
        val e1 = exponent(p.imag())
        val e2 = 1 / e1
        set(sinus(p.real()) * (e1 + e2) / 2, cosine(p.real()) * (e1 - e2) / 2)
        return this
    }

    fun cos(p: Complex): Complex {
        val e1 = exponent(p.imag())
        val e2 = 1 / e1
        set(cosine(p.real()) * (e1 + e2) / 2, -sinus(p.real()) * (e1 - e2) / 2)
        return this
    }
}