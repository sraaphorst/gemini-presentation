package s04

// *** FULLY IMMUTABLE EXAMPLE.
// *** USES RECURSION, FOLD EXPRESSIONS, AND DATA CLASSES.

import kotlin.math.min
import kotlin.random.*

// A type alias to refer to a team of Players.
typealias Team = List<Player>

// A class to maintain a counter for each of the Player types.
abstract class Counter {
    private var number = 1
    fun next(): Int {
        return number++
    }
}

// Player type superclass (abstract).
// hitPoints is mutable: everything else is immutable.
abstract class Player(val weapon: String,
                      private val minDamage: Int,
                      private val maxDamage: Int): Comparable<Player> {
    abstract val name: String
    abstract val hitPoints: Int

    // Kotlin knows that this is going to return an Int.
    fun damage() = Random.nextInt(minDamage, maxDamage + 1)

    // Kotlin knows this is going to "return Unit," i.e. essentially the lack of a value.
    abstract fun hit(damage: Int): Player

    fun isDead() = hitPoints <= 0

    override fun toString() =
        "$name (HP: $hitPoints)"

    /**
     * NOTE: COMPARABLE INTERFACE IMPLEMENTATION FOR SORTING BY NAME FOR FINAL RESULT.
     */
    override fun compareTo(other: Player) =
        name.compareTo(other.name)
}


/**
 * FORCES OF EVIL.
 */

// Goblin: A weak level enemy with 2-8 HP and a stick that does 1-3 damage.
data class Goblin(
    override val name: String = "Goblin ${next()}",
    override val hitPoints: Int = Random.nextInt(2, 6)): Player(
    "Stick",
    1,
    3) {

    override fun hit(damage: Int): Goblin =
        copy(name = name, hitPoints = hitPoints - damage)

    // We have a counter that makes sure the Goblins are named Goblin 1, Goblin 2, etc.
    companion object: Counter()
}

// Orc: A medium level enemy with 10-20 HP and a club that does 5-12 damage.
data class Orc(
    override val name: String = "Orc ${next()}",
    override val hitPoints: Int = Random.nextInt(10, 20)): Player(
    "Club",
    5,
    12) {

    override fun hit(damage: Int): Orc =
        copy(name = name, hitPoints = hitPoints - damage)

    companion object: Counter()
}

// Troll: A high level enemy with 24-60 HP and a hammer that does 12-20 damage.
data class Troll(
    override val name: String = "Troll ${next()}",
    override val hitPoints: Int = Random.nextInt(24, 60)): Player(
    "Hammer",
    12,
    20) {

    override fun hit(damage: Int): Troll =
        copy(name = name, hitPoints = hitPoints - damage)

    companion object: Counter()
}

/**
 * FORCES OF GOOD.
 */
data class Commoner(
    override val name: String = "Commoner ${next()}",
    override val hitPoints: Int = Random.nextInt(2, 4)): Player(
    "Hoe",
    3,
    6) {

    override fun hit(damage: Int): Commoner =
        copy(name = name, hitPoints = hitPoints - damage)

    companion object: Counter()
}

data class Knight(
    override val name: String = "Knight ${next()}",
    override val hitPoints: Int = Random.nextInt(20, 40)): Player(
    "Sword",
    8,
    16) {

    override fun hit(damage: Int): Knight =
        copy(name = name, hitPoints = hitPoints - damage)

    companion object: Counter()
}

data class Wizard(
    override val name: String = "Wizard ${next()}",
    override val hitPoints: Int = Random.nextInt(12, 24)): Player(
    "Fireball",
    16,
    40) {

    override fun hit(damage: Int): Wizard =
        copy(name = name, hitPoints = hitPoints - damage)

    companion object: Counter()
}

/**
 * Carry out one round of battle.
 * Print the results and return the survivors.
 * In this case, you do have to state the return type:
 * A Pair consisting of a List of Players of bad guys and a List of Players of good guys.
 */
fun fight(badGuys: Team, goodGuys: Team): Pair<Team, Team> {
    // Shuffle the bad guys and the good guys.
    // *** These lists are immutable, so they are not shuffled in place.
    // *** It is possible to shuffle a mutable list in place.
    val shuffledBadGuys = badGuys.shuffled()
    val shuffledGoodGuys = goodGuys.shuffled()

//    val mutableList = badGuys.toMutableList()
//    mutableList.shuffle()

    // Determine the size of the maximum remaining army.
    // We pair the bad guys and good guys off and fight them. Anyone not paired off advances.
    val minTeamSize = min(shuffledBadGuys.size, shuffledGoodGuys.size)

    // Pit the armies against each other.
    val fightingBadGuys = shuffledBadGuys.take(minTeamSize)
    val waitingBadGuys = shuffledBadGuys.drop(minTeamSize)
    val fightingGoodGuys = shuffledGoodGuys.take(minTeamSize)
    val waitingGoodGuys = shuffledGoodGuys.drop(minTeamSize)

    // Skirmish.
    val (updatedFightingBadGuys, updatedFightingGoodGuys) = fightingBadGuys
        .zip(fightingGoodGuys)
        .fold(Pair(emptyList<Player>(), emptyList<Player>())) { teams, players ->
        val (updatedBadGuys, updatedGoodGuys) = teams
        val (badGuy, goodGuy) = players

        println("$badGuy - VS - $goodGuy")

        // Determine the damage caused by both sides.
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

    // Sift out the casualties.
    val remainingBadGuys = updatedFightingBadGuys.filterNot { it.isDead() } + waitingBadGuys
    val remainingGoodGuys = updatedFightingGoodGuys.filterNot { it.isDead() } + waitingGoodGuys
    return Pair(remainingBadGuys, remainingGoodGuys)
}

fun main() {
    // Create the armies.
    val badGuys = ((10..20).map { Goblin() } +
            (5..10).map { Orc() } +
            (2..3).map { Troll() })

    val goodGuys = ((10..20).map { Commoner() } +
            (4..5).map { Knight() } +
            (1..3).map { Wizard() })

    println("BEGINNING ARMY: EVIL")
    badGuys.forEach { println(it) }
    println()

    println("BEGINNING ARMY: GOOD")
    goodGuys.forEach { println(it) }
    println()

    // Recurse instead of loop - preferably via tail recursion - to avoid mutability.
    tailrec fun aux(round: Int = 1,
                    currBadGuys: Team = badGuys,
                    currGoodGuys: Team = goodGuys): Pair<Team, Team> {
        return if (currBadGuys.isEmpty() || currGoodGuys.isEmpty())
            Pair(currBadGuys, currGoodGuys)
        else {
            println("*** BATTLE ROUND $round ***")
            println()
            val results = fight(currBadGuys, currGoodGuys)
            aux(round + 1, results.first, results.second)
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
