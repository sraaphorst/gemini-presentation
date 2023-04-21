package s03

// An object that caches factorial values.
private object Factorial {
    private val cache = mutableMapOf(0 to 1L)

    fun apply(n: Int): Long = when {
        n < 0 -> 0
        else -> cache.getOrPut(n) { n.toLong() * apply(n - 1) }
    }
}

fun factorial(n: Int) =
    Factorial.apply(n)

fun main() {
    (0..10).forEach { println(factorial(it)) }
}
