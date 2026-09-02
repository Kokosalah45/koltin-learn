package playground.utils


fun multiplyBy2Function(a: Int): Int {
    return a * 2
}

fun multiplyBy2SingleExpression(a: Int) = a * 2

val multiplyBy2Lambda: (Int) -> Int = { a -> a * 2 }

val multiplyBy2LambdaInferred = { a: Int -> a * 2 }


fun runFOO() {
    val x = (1..5).toList()
    println(
        """

        fun multiplyBy2Lambda(a: Int) = a * 2
        called via => x.map(multiplyBy2Lambda)
        Lambda: ${x.map(multiplyBy2Lambda)}

        """.trimIndent()
    )
    println(
        """

        fun multiplyBy2Function(a: Int): Int {
            return a * 2
        }
        called via => x.map(::multiplyBy2Function)
        Regular function: ${x.map(::multiplyBy2Function)}

        """.trimIndent()
    )
    println(
        """

        fun multiplyBy2SingleExpression(a: Int) = a * 2
        called via => x.map(::multiplyBy2SingleExpression)
        single expression: ${x.map(::multiplyBy2SingleExpression)}

        """.trimIndent()
    )
}

typealias myFilterPredicate<T> = (element: T, index: Int?, originalList: List<T>?) -> Boolean


fun <T> List<T>.myFilter(predicate: myFilterPredicate<T>): List<T> {

    val resultList = mutableListOf<T>()

    for ((index, item) in this.withIndex()) {
        if (predicate(item, index, this)) {
            resultList.add(item)
        }
    }

    return resultList
}

inline fun <T> List<T>.myFilterInline(predicate: myFilterPredicate<T>): List<T> {

    val resultList = mutableListOf<T>()

    for ((index, item) in this.withIndex()) {
        if (predicate(item, index, this)) {
            resultList.add(item)
        }
    }

    return resultList
}

fun runMyFilter() {
    val myArray = listOf(1, 2, 3, 4, 5)
    val filteredArray = myArray.myFilter { element, _, _ -> element % 2 == 0 }
    val filteredArrayInline = myArray.myFilterInline { element, _, _ -> element % 2 == 0 }
    println(filteredArray)
}
