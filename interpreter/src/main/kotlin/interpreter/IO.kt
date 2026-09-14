package interpreter

fun interface PrintEmitter {
    fun print(message: String)
}

fun interface InputProvider {
    fun readInput(prompt: String): String
}

fun interface EnvProvider {
    fun readEnv(key: String): String?
}

data class InterpreterIO(
    val emitter: PrintEmitter,
    val provider: InputProvider,
    val envProvider: EnvProvider = EnvProvider { null },
)
