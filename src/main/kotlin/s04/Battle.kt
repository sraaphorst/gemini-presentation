package s04

// *** FULLY IMMUTABLE EXAMPLE.
// *** USES RECURSION, FOLD EXPRESSIONS, AND DATA CLASSES.

import kotlin.math.min
import kotlin.random.Random

// A type alias to refer to a team of Players.
typealias Team = List<Player>

// A class to maintain a counter for each of the player types.
// Immutability present in this class as a counting mechanism.
abstract class Counter {
    private var number = 1
    fun next(): Int = number++
}

// Player abstract superclass.
abstract class Player(
    val weapon: String,
    private val minDamage: Int,
    private val maxDamage: Int
) : Comparable<Player> {
    abstract val name: String
    abstract val hitPoints: Int

    // Kotlin infers return types of non-abstract methods.
    fun damage() = Random.nextInt(minDamage, maxDamage + 1)

    abstract fun hit(damage: Int): Player

    fun isDead() = hitPoints <= 0

    override fun toString() = "$name (HP: $hitPoints)"

    // *** NOTE: Comparable interface implementation for sorting the final result by name.
    override fun compareTo(other: Player) = name.compareTo(other.name)
}

// Forces of evil
data class Goblin(
    override val name: String = "Goblin ${next()}",
    override val hitPoints: Int = Random.nextInt(2, 6)
) : Player("Stick", 1, 3) {
    override fun hit(damage: Int): Goblin = copy(hitPoints = hitPoints - damage)
    companion object : Counter()
}

data class Orc(
    override val name: String = "Orc ${next()}",
    override val hitPoints: Int = Random.nextInt(10, 20)
) : Player("Club", 5, 12) {
    override fun hit(damage: Int): Orc = copy(hitPoints = hitPoints - damage)
    companion object : Counter()
}

data class Troll(
    override val name: String = "Troll ${next()}",
    override val hitPoints: Int = Random.nextInt(24, 60)
) : Player("Hammer", 12, 20) {
    override fun hit(damage: Int): Troll = copy(hitPoints = hitPoints - damage)
    companion object : Counter()
}

// Forces of good
data class Commoner(
    override val name: String = "Commoner ${next()}",
    override val hitPoints: Int = Random.nextInt(2, 4)
) : Player("Hoe", 3, 6) {
    override fun hit(damage: Int): Commoner = copy(hitPoints = hitPoints - damage)
    companion object : Counter()
}

data class Knight(
    override val name: String = "Knight ${next()}",
    override val hitPoints: Int = Random.nextInt(20, 40)
) : Player("Sword", 8, 16) {
    override fun hit(damage: Int): Knight = copy(hitPoints = hitPoints - damage)
    companion object : Counter()
}

data class Wizard(
    override val name: String = "Wizard ${next()}",
    override val hitPoints: Int = Random.nextInt(12, 24)
) : Player("Fireball", 16, 40) {
    override fun hit(damage: Int): Wizard = copy(hitPoints = hitPoints - damage)
    companion object : Counter()
}

fun fight(badGuys: Team, goodGuys: Team): Pair<Team, Team> {
    val shuffledBadGuys = badGuys.shuffled()
    val shuffledGoodGuys = goodGuys.shuffled()
    val minTeamSize = min(shuffledBadGuys.size, shuffledGoodGuys.size)

    val (updatedFightingBadGuys, updatedFightingGoodGuys) = shuffledBadGuys
        .take(minTeamSize)
        .zip(shuffledGoodGuys.take(minTeamSize))
        .fold(Pair(emptyList<Player>(), emptyList<Player>())) { teams, players ->
            val (updatedBadGuys, updatedGoodGuys) = teams
            val (badGuy, goodGuy) = players

            println("$badGuy - VS - $goodGuy")

            val goodGuyDamage = goodGuy.damage()
            val badGuyDamage = badGuy.damage()

            println("${goodGuy.name} hits ${badGuy.name} with ${goodGuy.weapon} for $goodGuyDamage damage!")
            println("${badGuy.name} hits ${goodGuy.name} with ${badGuy.weapon} for $badGuyDamage damage!")
            val updatedBadGuy = badGuy.hit(goodGuyDamage)
            val updatedGoodGuy = goodGuy.hit(badGuyDamage)
            if (updatedBadGuy.isDead()) println("${updatedBadGuy.name} dies.")
            if (updatedGoodGuy.isDead()) println("${updatedGoodGuy.name} dies.")
            println()

            Pair(updatedBadGuys + updatedBadGuy, updatedGoodGuys + updatedGoodGuy)
        }

    val remainingBadGuys = updatedFightingBadGuys.filterNot { it.isDead() } + shuffledBadGuys.drop(minTeamSize)
    val remainingGoodGuys = updatedFightingGoodGuys.filterNot { it.isDead() } + shuffledGoodGuys.drop(minTeamSize)
    return Pair(remainingBadGuys, remainingGoodGuys)
}

fun main() {
    // Create the armies.
    val badGuys = List(14){ Goblin() } + List(5){ Orc() } + List(2){ Troll() }
    val goodGuys = List(10){ Commoner() } + List(2){ Knight() } + List(3){ Wizard() }

    println("BEGINNING ARMY: EVIL")
    badGuys.forEach { println(it) }
    println()

    println("BEGINNING ARMY: GOOD")
    goodGuys.forEach { println(it) }
    println()

    // Instead of loops, use (tail) recursive functions.
    tailrec fun aux(
        round: Int = 1,
        currBadGuys: Team = badGuys,
        currGoodGuys: Team = goodGuys
    ): Pair<Team, Team> {
        return when {
            currBadGuys.isEmpty() || currGoodGuys.isEmpty() -> Pair(currBadGuys, currGoodGuys)
            else -> {
                println("*** BATTLE ROUND $round ***")
                println()
                val results = fight(currBadGuys, currGoodGuys)
                aux(round + 1, results.first, results.second)
            }
        }
    }

    val (finalBadGuys, finalGoodGuys) = aux()
    val victors = finalBadGuys.ifEmpty { finalGoodGuys }
    println("*** VICTORS ***")
    if (victors.isEmpty())
        println("All are dead.")
    else
        victors.sorted().forEach { println(it) }
}
