package playground.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class Coll {

    private val _internalList = mutableListOf("1", "2")

    val list: ImmutableList<String>
        get() = _internalList.toImmutableList()
}

fun main() {

    val col = Coll()

    val bound = 6
    val progression = 1..<bound

    val evenListBuilder = buildList {
        progression.forEach { num ->
            if (num % 2 == 0) {
                add(num)
            }
        }
    }

    val evenListProgression = progression.filter { it % 2 == 0 }

    val evenListGenerator = List(bound) { if (it % 2 == 0) it else null }

    println("Builder: $evenListBuilder")
    println("Generator: $evenListGenerator")
    println("Progression filter: $evenListProgression")

    println("Immutable list: ${col.list}")
}
