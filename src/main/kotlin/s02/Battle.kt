package s02

import kotlin.math.min
import kotlin.random.*

// A class to maintain a counter for each of the Player types.
abstract class Counter {
    private var number = 1
    fun next(): Int {
        return number++
    }
}

// Player type superclass (abstract).
// hitPoints is mutable: everything else is fixed.
abstract class Player(val name: String,
                      minHitPoints: Int,
                      maxHitPoints: Int,
                      val weapon: String,
                      val minDamage: Int,
                      val maxDamage: Int,
                      var hitPoints: Int = Random.nextInt(minHitPoints, maxHitPoints)) {
    fun damage(): Int = Random.nextInt(minDamage, maxDamage + 1)

    fun hit(damage: Int) {
        hitPoints -= damage
    }

    fun isDead() = hitPoints <= 0

    override fun toString(): String =
        "$name (HP: $hitPoints)"
}


/**
 * FORCES OF EVIL.
 */

// Goblin: A weak level enemy with 2-8 HP and a stick that does 1-3 damage.
class Goblin: Player(
    "Goblin ${next()}",
    2,
    8,
    "Stick",
    1,
    3) {

    // We have a counter that makes sure the Goblins are named Goblin 1, Goblin 2, etc.
    companion object: Counter()
}

// Orc: A medium level enemy with 10-20 HP and a club that does 5-12 damage.
class Orc: Player(
    "Orc ${next()}",
    10,
    20,
    "Club",
    5,
    12) {
    companion object: Counter()
}

// Troll: A high level enemy with 24-60 HP and a hammer that does 12-20 damage.
class Troll: Player(
    "Troll ${next()}",
    24,
    60,
    "Hammer",
    12,
    20) {
    companion object: Counter()
}

/**
 * FORCES OF GOOD.
 */
class Peasant: Player(
    "Peasant ${next()}",
    2,
    4,
    "Hoe",
    3,
    6) {
    companion object: Counter()
}

class Knight: Player(
    "Knight ${next()}",
    20,
    40,
    "Sword",
    8,
    16) {
    companion object: Counter()
}

class Wizard: Player(
    "Wizard ${next()}",
    12,
    24,
    "Fireball",
    16,
    40) {
    companion object: Counter()
}

/**
 * Carry out one round of battle.
 * Print the results and return the survivors.
 */
fun fight(badGuys: List<Player>, goodGuys: List<Player>): Pair<List<Player>, List<Player>> {
    // Shuffle the bad guys and the good guys.
    val shuffledBadGuys = badGuys.shuffled()
    val shuffledGoodGuys = goodGuys.shuffled()

    // Determine the size of the maximum remaining army.
    // We pair the bad guys and good guys off and fight them. Anyone not paired off advances.
    val minTeamSize = min(shuffledBadGuys.size, shuffledGoodGuys.size)

    // Pit the armies against each other.
    val fightingBadGuys = shuffledBadGuys.take(minTeamSize)
    val waitingBadGuys = shuffledBadGuys.drop(minTeamSize)
    val fightingGoodGuys = shuffledGoodGuys.take(minTeamSize)
    val waitingGoodGuys = shuffledGoodGuys.drop(minTeamSize)

    // Skirmish.
    fightingBadGuys.zip(fightingGoodGuys).forEach { (badGuy, goodGuy) ->
        println("$badGuy - VS - $goodGuy")

        // Determine the damage caused by both sides.
        val goodGuyDamage = goodGuy.damage()
        val badGuyDamage = badGuy.damage()

        println("${goodGuy.name} hits ${badGuy.name} with ${goodGuy.weapon} for ${goodGuyDamage} damage!")
        println("${badGuy.name} hits ${goodGuy.name} with ${badGuy.weapon} for ${badGuyDamage} damage!")
        badGuy.hit(goodGuyDamage)
        goodGuy.hit(badGuyDamage)
    }

    // Sift out the casualties.
    fightingBadGuys.filter { it.isDead() }
        .forEach { println("${it.name} dies.") }
    fightingGoodGuys.filter { it.isDead() }
        .forEach { println("${it.name} dies.") }

    val remainingBadGuys = fightingBadGuys.filter { !it.isDead() } + waitingBadGuys
    val remainingGoodGuys = fightingGoodGuys.filter { !it.isDead() } + waitingGoodGuys
    return Pair(remainingBadGuys, remainingGoodGuys)
}

fun main() {
    // Create the armies.
    var badGuys = ((10..20).map { Goblin() } +
            (5..10).map { Orc() } +
            (2..3).map { Troll() })

    var goodGuys = ((10..20).map { Peasant() } +
            (4..5).map { Knight() } +
            (1..3).map { Wizard() })

    var round = 1
    while (!badGuys.isEmpty() && !goodGuys.isEmpty()) {
        println("*** BATTLE ROUND $round ***")
        val results = fight(badGuys, goodGuys)
        badGuys = results.first
        goodGuys = results.second
        println("\n")
        ++round
    }

    val victors = if (badGuys.isEmpty()) goodGuys else badGuys
    println("*** VICTORS ***")
    if (victors.isEmpty())
        println("All are dead.")
    else
        victors.forEach { println(it) }
}
