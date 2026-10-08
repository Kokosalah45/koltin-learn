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

// ============================================
// 8. DEFAULT PARAMETERS
// ============================================
/*
    You can provide default values for constructor parameters.
    Callers can omit them—they'll use the default.
    Very useful for optional configuration.
*/
class User(
    val name: String,
    val email: String = "no-email@example.com",  // has default
    val age: Int = 0,                             // has default
    val role: String = "user"                     // has default
)

// Usage:
// val user1 = User("Alice", "alice@example.com", 25, "admin")
// val user2 = User("Bob")  // Uses all defaults except name
// val user3 = User("Charlie", age = 30)  // Only override age

// ============================================
// 9. NAMED ARGUMENTS
// ============================================
/*
    Instead of positional arguments, you can use parameter names.
    Makes code more readable, especially with many parameters.
    Can pass arguments in any order.
*/
class Product(val name: String, val price: Double, val inStock: Boolean, val category: String) {
    override fun toString() = "$name ($category): $$price - In Stock: $inStock"
}

// Usage:
// val product1 = Product("Laptop", 999.99, true, "Electronics")  // positional
// val product2 = Product(name = "Laptop", price = 999.99, inStock = true, category = "Electronics")  // named
// val product3 = Product(category = "Electronics", name = "Phone", price = 599.99, inStock = true)  // any order!

// ============================================
// 10. CUSTOM SETTERS
// ============================================
/*
    Just like custom getters, you can define custom setters.
    Use this for validation or computed properties that need assignment.
    Only works with var (mutable properties), not val.
*/
class BankAccount(initialBalance: Double = 0.0) {
    var balance: Double = initialBalance
        // Custom getter (optional)
        get() {
            println("Checking balance...")
            return field  // 'field' is the backing field
        }
        // Custom setter (optional)
        set(value) {
            if (value < 0) {
                println("Cannot set negative balance!")
                return
            }
            println("Setting balance to $value")
            field = value  // 'field' holds the actual value
        }
}

class BankAccountIdiomatic(private var _balance: Double = 0.0) {
    var balance: Double
        // Custom getter (optional)
        get() {
            println("Checking balance...")
            return _balance  // 'field' is the backing field
        }
        // Custom setter (optional)
        set(value) {
            if (value < 0) {
                println("Cannot set negative balance!")
                return
            }
            println("Setting balance to $value")
            _balance = value  // 'field' holds the actual value
        }
}

// Usage:
// val account = BankAccount(100.0)
// account.balance = 150.0  // Runs the setter, prints "Setting balance to 150.0"
// account.balance = -50.0  // Runs the setter, prints "Cannot set negative balance!"
// val current = account.balance  // Runs the getter, prints "Checking balance..."

// ============================================
// ENCAPSULATION: Why private + getter/setter > public property
// ============================================
/*
    Key principle: If you make something PUBLIC, you lock yourself into that implementation.
    If you use PRIVATE + getter/setter, you can change the implementation later without
    breaking code that uses your class.

    This is critical for configuration, user input, and state management.
*/

// ❌ BAD: Public property - can't add validation later without breaking code
class ConfigBad {
    var apiKey: String = ""  // Anyone can set: config.apiKey = "" (empty!)
    var maxRetries: Int = 3  // Anyone can set: config.maxRetries = -1 (negative!)
}

// ✅ GOOD: Private + getter/setter - you control the behavior
class ConfigGood {
    private var _apiKey: String = ""
    private var _maxRetries: Int = 3

    var apiKey: String
        get() = _apiKey
        set(value) {
            // Validation: prevent empty API keys
            if (value.isBlank()) {
                throw IllegalArgumentException("API key cannot be empty")
            }
            // Normalization: trim whitespace, convert to uppercase
            _apiKey = value.trim().uppercase()
        }

    var maxRetries: Int
        get() = _maxRetries
        set(value) {
            // Validation: prevent negative retries
            if (value < 0) {
                throw IllegalArgumentException("Retries cannot be negative")
            }
            _maxRetries = value
        }
}

// Usage:
// val config = ConfigGood()
// config.apiKey = "my-api-key"  // ✅ Works, automatically trimmed & uppercase
// config.apiKey = ""  // ❌ Throws error (validated!)
// config.maxRetries = 5  // ✅ Works
// config.maxRetries = -1  // ❌ Throws error (validated!)

// ============================================
// REAL-WORLD EXAMPLES
// ============================================

// Example 1: Email validation
class Userr {
    private var _email: String = ""

    var email: String
        get() = _email
        set(value) {
            val isValid = value.contains("@") && value.contains(".")
            if (!isValid) throw IllegalArgumentException("Invalid email format")
            _email = value
        }
}

// Example 2: Password field (never expose raw value)
class LoginForm {
    private var _password: String = ""

    var password: String
        get() = ""  // NEVER return the actual password!
        set(value) {
            if (value.length < 8) throw IllegalArgumentException("Password must be 8+ chars")
            _password = value  // Store securely (in real app, hash it)
        }
}

// Example 3: Auto-normalization
class UserProfile {
    private var _username: String = ""

    var username: String
        get() = _username
        set(value) {
            // Auto-normalize: remove spaces, lowercase
            _username = value.trim().lowercase()
        }
}

// Usage:
// val profile = UserProfile()
// profile.username = "  John Doe  "
// println(profile.username)  // Output: "john doe" (auto-normalized)

// Example 4: Observable field (notify when changed)
class Observable {
    private var _value: String = ""
    private val listeners = mutableListOf<(String) -> Unit>()

    var value: String
        get() = _value
        set(newValue) {
            if (newValue != _value) {  // Only notify if actually changed
                _value = newValue
                listeners.forEach { it(_value) }  // Notify all observers
            }
        }

    fun subscribe(listener: (String) -> Unit) {
        listeners.add(listener)
    }
}

// Usage:
// val obs = Observable()
// obs.subscribe { println("Value changed to: $it") }
// obs.value = "new data"  // Prints: "Value changed to: new data"

// ============================================
// THE BOTTOM LINE
// ============================================
/*
    Public property:  var name: String = ""
    ↓
    Forces you to commit to direct field access.
    If you need validation/normalization later, you break the API.

    Private + getter/setter:  var name: String get() = ... set(value) { ... }
    ↓
    You can change implementation anytime without breaking code.
    This is how professional, maintainable code is written.

    ALWAYS use private + getter/setter for:
    - Configuration fields
    - User input
    - Sensitive data (passwords, tokens)
    - Fields that need validation
    - Fields that should notify observers when changed
*/

// ============================================
// 11. DATA CLASS FEATURES
// ============================================
/*
    Data classes auto-generate: equals(), hashCode(), toString(), copy()
    They also support destructuring and component functions.
*/
data class Book(val title: String, val author: String, val pages: Int)

// Usage:
// val book1 = Book("1984", "Orwell", 328)

// 1. toString() - auto-generated, very readable
// println(book1)  // Output: Book(title=1984, author=Orwell, pages=328)

// 2. equals() - compares by value, not reference
// val book2 = Book("1984", "Orwell", 328)
// println(book1 == book2)  // true (same content, even different objects)

// 3. copy() - create a modified copy without changing the original
// val book3 = book1.copy(pages = 350)  // Same title and author, new page count
// val book4 = book1.copy(title = "Animal Farm")  // New title, same author and pages

// 4. Destructuring - unpack properties into variables
// val (title, author, pages) = book1
// println("$title by $author ($pages pages)")

// 5. Component functions - access properties by position
// val firstProperty = book1.component1()  // title
// val secondProperty = book1.component2()  // author
// val thirdProperty = book1.component3()  // pages

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
