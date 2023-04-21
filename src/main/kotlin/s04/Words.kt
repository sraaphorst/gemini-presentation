package s04

import kotlin.random.*

enum class Species {
    CAT,
    DOG,
    OTHER
}

data class Client(
    val ownerName: String,
    val petName: String,
    val species: Species,
    val age: Int
)


// Put together a little database of veterinary clients.
val clients = listOf(
    Client("Jen Miller", "Purri", Species.CAT, 4),
    Client("Sebastian Raaphorst", "Kali", Species.CAT, 6),
    Client("Sebastian Raaphorst", "Duncan", Species.CAT, 13),
    Client("John Strauss", "Slithers", Species.OTHER, 11),
    Client("Anne Holmes", "Rover", Species.DOG, 8)
)

fun main() {
    // Find the first name of first client with a cat under 5 years old.
    val youngCatOwner = clients
        .filter { it.species == Species.CAT }
        .filter { it.age < 5 }
        .map { it.ownerName }
        .firstOrNull()
        ?.split(' ') ?: "no such client."
    print("Search for owner of cat under 5 years old yielded: $youngCatOwner")

    // Iterate over the cats and print each one's name and then determine which is the senior.
    val oldestCat = clients
        .filter { it.species == Species.CAT }
        .also { println("Cat found: $it") }
        .maxByOrNull { it.age }
    if (oldestCat != null)
        print("Oldest cat is ${oldestCat.petName}.")
}