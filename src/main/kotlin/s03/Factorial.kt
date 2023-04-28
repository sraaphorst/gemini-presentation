package s03

// *** MEMOIZATION
// An singleton object that caches factorial values.
private object Factorial {
    // Cannot change the reference cache, but we can change the data contained in the container.
    private val cache = mutableMapOf(0 to 1L)

    fun apply(n: Int): Long = when {
        n < 0 -> 0
        else -> cache.getOrPut(n) { n.toLong() * apply(n - 1) }
    }
}

fun factorial(n: Int) =
    Factorial.apply(n)

fun main() {
    (0..12).forEach { println(factorial(it)) }
}
