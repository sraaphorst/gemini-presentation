# gemini-presentation

Presentation on Kotlin basics for my fellow team members.

* [The sad state of Java, aka why do we still bother?](src/main/java/s01)
* [Kotlin (Scala, Clojure, etc). The non-decaffeinated Java.](src/main/kotlin/s01)
* [Lambdas and Nullability](src/main/kotlin/s02)
* [Pimping Functions, Generators, Memoization](src/main/kotlin/s03)
* [Battle Royale: Immutability Rocks](src/main/kotlin/s04)

Some brief notes on functional programming:
* We aim for immutability of data types and collections if possible.
* Instead, data is filtered, transformed, etc. into new collections or objects.
* Side effects (e.g. as is common in OOP) are undesirable.
* Referential transparency is a key idea: if we have a block of code:
```kotlin
performAction(myObject)
```
then we should be able to copy the code for `performAction`, substituting `myObject`
inside for the parameter, and the result should be identical no matter how many times
we invoke the statement, i.e. `myObject` is not affected and the return value should be
consistent.
* Side effects (which are necessary, e.g. printing to the screen, rendering UIs, reading in data) are
confined and then "unleashed" at the end of setting everything up, which is called the "end of the world."

## Functional Programming, according to ChatGPT:

Functional programming is a programming paradigm that emphasizes the use of functions to solve problems. The principles of functional programming include:

Immutability: Functional programming advocates immutability, meaning that once a value is assigned to a variable, it cannot be changed. Instead of modifying a value in place, a new value is created based on the old value and any modifications needed.

Pure functions: A pure function is a function that has no side effects and always returns the same output for a given input. Pure functions make it easier to reason about code and prevent unexpected behavior.

Higher-order functions: A higher-order function is a function that takes one or more functions as arguments and/or returns a function as its result. Higher-order functions enable code reuse and make it possible to write more generic and flexible code.

Recursion: Recursion is the process of defining a function in terms of itself. Functional programming heavily uses recursion to solve problems that involve repetitive processing.

Declarative programming: In declarative programming, the focus is on describing what needs to be done, rather than how it should be done. This makes it easier to reason about code and reduces the risk of introducing bugs.

Lazy evaluation: Lazy evaluation is a technique that defers the evaluation of an expression until its value is actually needed. This can help reduce memory usage and improve performance in some cases.

These principles work together to promote code that is easy to understand, test, and maintain. Functional programming is becoming increasingly popular, especially in areas such as data processing and scientific computing.

### Fin.