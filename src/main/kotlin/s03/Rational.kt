package s03

// *** OPERATOR OVERLOADING, CLASS PIMPING, AND INTERFACES

import kotlin.math.absoluteValue
import kotlin.math.sign

// Representation of a rational number. Note no "val" here, so num and denom are not stored.
class Rational(num: Int, denom: Int): Comparable<Rational> {
    // Secondary constructor: for integers.
    constructor(num: Int): this(num, 1)

    // Init block: perform checks and setup.
    init {
        if (denom == 0)
            throw IllegalArgumentException("Denominator cannot be zero.")
    }

    // Initialize the actual member variables.
    // We want any negative signs on the numerator, and we want the reduced fraction.
    val numerator = denom.sign * num / gcd(num, denom).absoluteValue
    val denominator = denom.absoluteValue / gcd(num, denom).absoluteValue

    // *** Operations to perform on Rationals. ***

    // *** UNARY MINUS ***
    // -(n/d) = -n/d
    operator fun unaryMinus() =
        Rational(-numerator, denominator)

    // *** ADDITION ***
    // n1/d1 + n2/d2 = (n1 * d2 + n2 * d1)/(d1 * d2)
    operator fun plus(r: Rational) =
        Rational(
            numerator * r.denominator + r.numerator * denominator,
            denominator * r.denominator
        )

    // n1/d1 + n = (n1 + n * d1)/d1
//    operator fun plus(n: Int) =
//        Rational(numerator + n * denominator, denominator)

    // Using pimping. See below.
    operator fun plus(n: Int) =
        this + n.toRational()

    // *** SUBTRACTION: Use unary minus and addition ***
    // n1/d1 - n2/d2 = n1/d1 + (- n2/d2)
    operator fun minus(r: Rational) =
        this + (-r)

    operator fun minus(n: Int) =
        this - n.toRational()

    // *** MULTIPLICATION ***
    // n1/d1 * n2/d2 = (n1 * n2)/(d1 * d2)
    operator fun times(r: Rational) =
        Rational(numerator * r.numerator, denominator * r.denominator)

    // MULTIPLICATION ON THE RIGHT BY AN INTEGER
    // Standard way:
    // n1/d1 * n = (n1 * n)/d1
//    operator fun times(n: Int) =
//        Rational(numerator * n, denominator)

    // Using pimping:
    operator fun times(n: Int) =
        this * n.toRational()

    // *** DIVISION ***
    // (n1/d1) / (n2/d2) = (n1 * d2)/(n2 * d1)
    operator fun div(other: Rational) =
        Rational(numerator * other.denominator, other.numerator * denominator)

    // Standard way:
    // (n1/d1) / n = n1 / (d1 n)
//    operator fun div(n: Int) =
//        Rational(numerator, n * denominator)

    // Using pimping:
    operator fun div(n: Int) =
        this / n.toRational()

    // Convert to Double.
    fun toDouble() =
        numerator.toDouble() / denominator.toDouble()

    // *** WHEN CLAUSE
    // Comparing.
    override operator fun compareTo(other: Rational): Int = when {
        toDouble() - other.toDouble() < 0 -> -1
        toDouble() - other.toDouble() > 0 -> 1
        else -> 0
    }

    // Determine if it is an integer.
    fun isInteger() =
        denominator == 1

    fun toInteger() =
        if (!isInteger())
            throw NumberFormatException("Denominator is not 1.")
        else
            numerator

    // Not a data class, so we do not get:
    // 1. toString
    // 2. equals
    // 3. hashCode
    // for free.
    override fun toString(): String =
        "$numerator/$denominator"

    // Compiler type information.
    override fun equals(other: Any?): Boolean {
        // Check if they reference the same object.
        if (this === other) return true

        // Check that they are the same class.
        if (javaClass != other?.javaClass) return false

        // *** COMPILER CASTING
        // Compiler does not know that other is Rational, so tell compiler to treat other as Rational.
        other as Rational
        return numerator == other.numerator && denominator == other.denominator
    }

    override fun hashCode(): Int {
        return 31 * numerator + denominator
    }

    companion object {
        // *** TAIL RECURSIVE FUNCTIONS
        // Tail recursive function to find the greatest common denominator of two integers.
        private tailrec fun gcd(a: Int, b: Int): Int =
            if (b == 0)
                a
            else
                gcd(b, a % b)
    }
}


// *** PIMPING OPERATIONS ***

// Add a method to the Int class to turn it into a rational.
fun Int.toRational() =
    Rational(this)

// MULTIPLICATION OF INTEGER WITH RATIONAL
// Commutative, so we can just flip.
operator fun Int.times(r: Rational) =
    r * this

// ADDITION OF INTEGER WITH RATIONAL
// Commutative, so we can just flip.
operator fun Int.plus(r: Rational) =
    r + this

// DIVISION OF INTEGER BY RATIONAL
// Non-commutative, so we convert Int to Rational and then defer to Rational method.
operator fun Int.div(r: Rational) =
    this.toRational() / r

// SUBTRACTION OF INTEGER BY RATIONAL
// Non-commutative, so we convert Int to Rational and then defer to Rational method.
operator fun Int.minus(r: Rational) =
    this.toRational() - r


fun main() {
    val r1 = Rational(8, -24)
    println("8/-24 = $r1")

    val r2 = Rational(-123, -456)
    println("-123/-456 = $r2")

    println("2 * $r2 = ${2 * r2} = ${r2 * 2}")
}