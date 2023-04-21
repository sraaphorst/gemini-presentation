package s02

// *** Nullability

enum class Species(val legalInHawaii: Boolean) {
    CAT(true),
    DOG(true),
    SNAKE(false)
}

data class Client(
    val staffMember: String,
    val petName: String,
    val species: Species,
    val age: Int
)


// *** Small database of pets with their primary staff member.
val clients = listOf(
    Client("Jen Miller",          "Purri",    Species.CAT,    4),
    Client("Sebastian Raaphorst", "Duncan",   Species.CAT,   13),
    Client("Sebastian Raaphorst", "Kali",     Species.CAT,    6),
    Client("John Strauss",        "Slithers", Species.SNAKE, 11),
    Client("Anne Holmes",         "Rover",    Species.DOG,    8)
)

fun main() {
    // *** LAMBDA EXPRESSIONS
    // Find any pets that are not legal in Hawaii.
    clients
        .filterNot { it.species.legalInHawaii }
        .forEach { println("${it.petName} is not legal in Hawaii!")}
    println()

    // *** Nullability following through.
    // *** Comment out Jen.
    // Find the first name of first client with a cat under 5 years old.
    val youngCatOwner = clients
        .filter { it.species == Species.CAT }
        .filter { it.age < 5 }
        .map { it.staffMember }
        .firstOrNull()
        ?.split(' ')
        ?.firstOrNull() ?: "no such client."
    println("Search for owner of cat under 5 years old yielded: $youngCatOwner")
    println()

    // *** Nullability checking.
    // Iterate over the cats and print each one's name and then determine which is the senior.
    val oldestCat = clients
        .filter { it.species == Species.CAT }
        .also { cats -> println("Cats found: ${cats.map(Client::petName).joinToString(", ")}") }
        .maxByOrNull { it.age }

    // *** Comment out line.
    if (oldestCat != null)
        print("Oldest cat is ${oldestCat.petName}.")
}
