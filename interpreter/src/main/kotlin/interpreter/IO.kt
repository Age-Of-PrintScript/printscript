package interpreter

fun interface PrintEmitter {
    fun print(message: String)
}

fun interface InputProvider {
    fun readInput(prompt: String): String
}

data class InterpreterIO(
    val emitter: PrintEmitter,
    val provider: InputProvider,
)
