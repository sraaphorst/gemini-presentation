package s02

// *** Nullability

enum class Species(val legalInHawaii: Boolean) {
    CAT(true),
    DOG(true),
    SNAKE(false)

}

// Data classes have many nice features:
// 1. All data members accessible.
// 2. Can be deconstructed (see below).
// 3. Has a copy method to create a new instance with some data changed.
// 4. Java isEqual, hashCode, and toString are all automatically defined to make sense.
data class Client(
    val petOwner: String,
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
    // Deconstruction of a data class. Note that we might not know if there are any clients, so we use nullable types.
    // firstPet is a Client?, meaning that it could be null.
    val firstPet = clients.firstOrNull()

    // Methods are usually called with the standard . exception.
    // Methods that can be null are called with ?. and only execute if the left-hand expression is not null.
    // ?: is called the ELVIS OPERATOR and if the LHS is null, it executes the RHS.
    firstPet?.apply {
        val (petOwner, petName, species, age) = clients.first()
        println("First pet: $petName (as owned by $petOwner) is a $age year old ${species.name}.")
    } ?: println("There is no first client.")
    println()

    // The Elvis expression is good for default values and reacting to errors, but many ways to do this.
    // This changes the type of secondPet from Client? (possibly null client) to Client (guaranteed not null).
    val secondClient = clients.getOrNull(1) ?: throw ArrayIndexOutOfBoundsException("No second pet!")
    println("The second Client on our list is: $secondClient")
    println()

    // *** LAMBDA EXPRESSIONS
    // Find any pets that are not legal in Hawaii.
    // Common FP behavior: chain together calls to transform / filter data until you get what you want.
    // This is done with LAMBDA expressions (typically { ... } where it refers to the input).

    // The data is immutable here:
    // 1. filterNot returns a new List<Client>
    // 2. forEach is a consumer and returns Unit, which means "no value" (slightly similar to void in Java but
    //    subtly different).
    clients
        .filterNot { it.species.legalInHawaii }
        .forEach { println("${it.petName} is not legal in Hawaii!")}
    println()

    // *** Nullability following through.
    // Find the first name of first client with a cat under 5 years old.
    // *** Comment out Jen and try again.
    val youngCatOwner = clients
        .filter { it.species == Species.CAT }
        .filter { it.age < 5 }
        .map { it.petOwner }
        .firstOrNull()
        ?.split(' ')
        ?.firstOrNull() ?: "no such client."
    println("Search for owner of cat under 5 years old yielded: $youngCatOwner")
    println()

    // *** Nullability checking.
    // Iterate over the cats and print each one's name and then determine which is the senior.

    // also: can be used for debugging (part of group of functions called SCOPE FUNCTIONS)
    // https://kotlinlang.org/docs/scope-functions.html
    val oldestCat = clients
        .filter { it.species == Species.CAT } // List<Client>
        .also {
            // it does not have to be used: can rename as in here with "clientsWithCats ->"
            clientsWithCats -> // List<Client>

            // Another way of invoking a function in chained statements: Client::petName is the same as the lambda:
            // { it.petName }
            val catString = clientsWithCats.map(Client::petName).joinToString(", ") // String

            println("Cats found: $catString")

            // The lambda returns nothing, but the List<Client> is passed along se we can continue to chain.
        }
        .maxByOrNull { it.age }

    // *** Compiler null checking: comment out the "if..." line.
    if (oldestCat != null)
        println("Oldest cat is ${oldestCat.petName}.")

    // Not great practice, but if you are sure something will not be null, you can inform the compiler using !!:
    println("We know there is an oldest cat, and it is: ${oldestCat!!.petName}.")
}
