package s03

import kotlin.math.absoluteValue
import kotlin.random.Random

// Sequences, lazy evaluation, fold, sorting.

fun main() {
    // Calculate the limit of the series 1/0! + 1/1! + 1/2! + 1/3!
    val eTerms = 20
    val e = generateSequence(0) { it + 1 }
        .map { t -> Rational(1, factorial(t).toInt()) }
        .take(eTerms)
        .sumOf { it.toDouble() }

    println("Value of e: ${Math.E}")
    println("Approximation of e using $eTerms terms: $e")
    println("Error: ${(Math.E - e).absoluteValue / Math.E}")
    println()

    // Calculate the limit of the series 1 + 1/2 + 1/4 + 1/8 + 1/16 + ...
    println("Summing sequence 1/1 + 1/2 + 1/3 + 1/4 + ...")
    val approach2 = generateSequence(1) { it * 2 }
        .map { t -> Rational(1, t) }
        .take(Int.SIZE_BITS / 2 - 1)
        .sumOf { it.toDouble() }
    println("Approximation of 2: $approach2")
    println("Error: ${(2.0 - approach2).absoluteValue / 2.0}")
    println()

    // Calculate the product of the series 1/2 * 2/3 * 3/4 * 4/5 * 5/6 * 6/7...
    val number = 20
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
    val minNum = -50
    val maxNum = 50
    val minDenom = -50
    val maxDenom = 50
    println("Generating 20 random rational numbers and sorting them:")
    val sortedList = generateSequence { Pair(Random.nextInt(minNum, maxNum), Random.nextInt(minDenom, maxDenom)) }
        .filter { it.second != 0 }
        .map { (n, d) -> Rational(n, d) }
        .take(20)
        .sorted()
        .toList()
    println("Sorted fractions: ${sortedList.map { if (it.isInteger()) it.toInteger() else it }}")
}
