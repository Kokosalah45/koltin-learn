package playground.main

import playground.utils.myForLoop
import playground.utils.runFOO
import playground.utils.runMyFilter
import playground.utils.Car
import playground.utils.HTTPStatus
import playground.utils.Pizza
import playground.utils.Subscriber


fun main() {
    myForLoop()
    runFOO()
    runMyFilter()

    val car = Car("Geely Coolray")
    val sub =
        Subscriber(
            name = "kero",
            email = "kokp.amged@gmail.com",
            birthDate = "2000-06-03"
        )

    println(sub.age)
    println(HTTPStatus.OK.toResponseString())

    val defaultPizza = Pizza.createDefaultPizza()

    println(defaultPizza.size)
    println(defaultPizza.toppings)

}
