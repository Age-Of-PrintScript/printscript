package executor

import domain.Failure
import domain.Success

fun main(){
    val executor = Executor()
    val result = executor.execute("let x: number = 5;")
    when (result) {
        is Failure -> {
            println("Fallo la puta madre: " + result.value)
        }
        is Success -> {
            println("Funciono? " + result.value.toString())
        }
    }
}