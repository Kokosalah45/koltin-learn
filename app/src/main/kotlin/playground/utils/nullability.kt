package playground.utils

class Person(val name: String, val age: Int?) {
    fun isAdult(): Boolean {
        return age != null && age >= 18
    }
}


fun <T> myRequireNotNull(value: T?, message: () -> String): T {
    return value ?: throw IllegalArgumentException(message())
}

fun <T> myCheckNotNull(value: T?, message: () -> String): T {
    return value ?: throw IllegalStateException(message())
}

fun <T> run() {
    val person: Person? = null
    lateinit var person2: Person
    val validatedPerson =
        myRequireNotNull<Person>(value = person) { "person must not be null" }
    val validatedName = validatedPerson.name
    println(validatedName)

    person2 = validatedPerson
    println(person2.name)
}
