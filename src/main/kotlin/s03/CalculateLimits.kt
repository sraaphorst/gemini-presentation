package s03

import kotlin.random.Random

// Sequences, lazy evaluation, fold, sorting.

fun main() {
    // Calculate the limit of the series 1/0! + 1/1! + 1/2! + 1/3!
    val eterms = 20
    val e = generateSequence(0) { it + 1 }
        .map { t -> Rational(1, factorial(t).toInt()) }
        .take(eterms)
        .sumOf { it.toDouble() }

    println("Value of e: ${Math.E}")
    println("Approximation of e using $eterms terms: $e")
    println("Error: ${100 * (Math.E - e)/Math.E} %")
    println()

    // Calculate the limit of the series 1 + 1/2 + 1/4 + 1/8 + 1/16 + ...
    println("Summing sequence 1/1 + 1/2 + 1/3 + 1/4 + ...")
    val approach2 = generateSequence(1) { it * 2 }
        .map { t -> Rational(1, t) }
        .take(Int.SIZE_BITS / 2 )
        .sumOf { it.toDouble() }
    println("Approximation of 2: $approach2")
    println()

    // Calculate the product of the series 1/2 * 2/3 * 3/4 * 4/5 * 5/6 * 6/7...
    val number = 11
    println("Product of sequence 1/2 * 2/3 * 3/4 * ... * ${number-1}/$number")
    val fraction = generateSequence(1) { it + 1 }
        .map { t -> Rational(t, t+1) }
        .take(number - 1)
        .fold(Rational(1)) { acc, curr ->
            acc * curr
        }
    println("Result is: $fraction")
    println()

    // Create a list of 20 rational numbers and sort them.
    println("Generating 20 random rational numbers and sorting them:")
    val sortedList = List(20) { Rational(Random.nextInt(-50, 50), Random.nextInt(-50, 50))}
        .sorted()
    println("Sorted fractions: ${sortedList.map { if (it.isInteger()) it.toInteger() else it }}")
}
