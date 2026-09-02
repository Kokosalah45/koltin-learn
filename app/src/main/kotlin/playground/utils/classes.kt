package playground.utils

import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

// ============================================
// 1. VERBOSE WAY: Manual property assignment
// ============================================
/*
    The long way to define a property from a constructor parameter.
    You explicitly declare a property and copy the parameter value to it.
*/
class VerboseClass {
    val x: String

    constructor(x: String) {
        this.x = x
    }
}

// ============================================
// 2. MODERN SHORTHAND: Property in constructor (RECOMMENDED)
// ============================================
/*
    When you declare val/var in the constructor, Kotlin automatically creates a property.
    This is the idiomatic Kotlin way—concise and clear.
    The compiler does the verbose work above for you automatically.
*/
class ShorthandClass(val x: String)

// ============================================
// 3. JUST A PARAMETER: Local variable only (NOT a property)
// ============================================
/*
    When you declare a parameter without val/var, it's just a local variable.
    It's only accessible inside the init block, not stored as a property.
    Use this when you need the value only during initialization (rare).
*/
class ParameterOnlyClass(x: String) {
    var myX: String = x  // Copy it to a property if you need it after init
}

// ============================================
// 4. LATEINIT: Delay property initialization
// ============================================
/*
    Use lateinit for properties that will be set later (not in constructor).
    Only works with var, not val.
    Useful when a property depends on something that's not available yet.
*/
class NormalClassLateInit {
    lateinit var myX: String

    constructor() {
        myX = "initialized after constructor"
    }
}

// ============================================
// 5. INTERFACES & INHERITANCE
// ============================================
/*
    An interface defines a contract (what methods must exist).
    A class can implement an interface with : Killer.
    It must override all abstract functions from the interface.
*/
interface Killer {
    fun kill(): Boolean
}

// ============================================
// 6. INIT BLOCK & PROPERTIES WITH CUSTOM GETTERS
// ============================================
/*
    - init block: runs immediately after constructor, used for validation/setup
    - private val: property only accessible inside the class
    - custom getter (val currentName: String get() = name): computed property
    - override: implementing a method from the interface
*/
class Car(private val name: String) : Killer {
    // Init block runs right after the constructor
    init {
        check(name.length <= 15) { "Car Name must not be more than 15 characters" }
    }

    var list = (1..5).toList()

    // Implement the interface method
    override fun kill(): Boolean = name.length % 2 == 0

    // Custom getter: computed property (runs every time it's accessed)
    val currentName: String
        get() = name
}

// ============================================
// 7. MULTIPLE CONSTRUCTORS
// ============================================
/*
    Primary constructor: defined in class declaration
    Secondary constructors: defined in the class body (less common in Kotlin)

    Use : this() to call another constructor (must call primary constructor eventually)
    Private constructors: can't instantiate directly, must use companion object factory
*/
class Pizza private constructor(val size: String, val toppings: List<String>) {

    // Primary constructor runs first
    init {
        println("Primary constructor: building a $size pizza with $toppings")
    }

    // Secondary constructor #1: provides default toppings
    private constructor(size: String) : this(size, listOf("cheese")) {
        println("Secondary #1: defaulted toppings to cheese")
    }

    // Secondary constructor #2: provides defaults for both size and toppings
    private constructor() : this("medium") {
        println("Secondary #2: defaulted size to medium")
    }

    // Companion object: static methods (factory pattern)
    companion object {
        fun createDefaultPizza() = Pizza()
    }
}

/*
    data classes
    - should be used to group data (mostly primitive data to avoid referential mismatch) only and manipulate their shape like formatiing or something
    - data classes are computed by value so the class contents not by reference
    - you need internal state but you dont want to manipulate them again just grouping data
*/

data class PersonDate(
    val years: Long,
    val months: Long,
    val days: Long,
)

data class Subscriber(
    val name: String,
    val email: String,
    val birthDate: String
) {
    val age: PersonDate
        // custom getter
        get() {
            val birthDateParsed = LocalDate.parse(birthDate)
            val now = ZonedDateTime.now()
            return PersonDate(
                years = ChronoUnit.YEARS.between(birthDateParsed, now),
                months = ChronoUnit.MONTHS.between(birthDateParsed, now),
                days = ChronoUnit.DAYS.between(birthDateParsed, now),
            )
        }
}

/*
    enum classes -> provides constant values
     - provide defined set of values
     - all declared params must be provided  because at the end of the day they are constants so all must exist at compile time
*/
enum class HTTPStatus(val code: Int, val message: String) {
    OK(200, "OK"),
    BAD_REQUEST(400, "BAD REQUEST"),
    UNEXPECTED_ERROR(500, "UNEXPECTED ERROR"),
    NOT_FOUND(404, "NOT FOUND");

    fun toResponseString() = "Error $code: $message"
}

/*
    this is enum class with no params provided
*/
enum class ErrorSeverity { MINOR, MAJOR, CRITICAL }


data class SuccessResponse<T>(val message: String? = "Success", val data: T? = null)

sealed class ResponseState<TSuccess, TError : Exception> {
    data class Success<TSuccess>(val response: SuccessResponse<TSuccess>) :
        ResponseState<TSuccess, Nothing>()

    data class Error<TError : Exception>(val error: TError) : ResponseState<Nothing, TError>()
}
