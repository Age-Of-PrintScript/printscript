package interpreter

fun interface InterpreterPrintEmitter {
    fun print(message: String)
}

fun interface InterpreterInputProvider {
    fun readInput(prompt: String): String
}

fun interface EnvProvider {
    fun readEnv(key: String): String?
}

data class InterpreterIO(
    val emitter: InterpreterPrintEmitter,
    val provider: InterpreterInputProvider,
    val envProvider: EnvProvider = EnvProvider { null },
)
