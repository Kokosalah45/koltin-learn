package playground.utils

import com.sun.swing.internal.plaf.metal.resources.metal

fun myForLoop() {

    val range = 0..<10 step 2
    val rangeDown = 10 downTo 0 step 2
    val wholeRange = 0..10 step 2
    println("Range:")
    for (i in range) {
        println(i)
    }

    println("Range down:")

    for (i in rangeDown) {
        println(i)
    }
}
