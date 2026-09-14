package engine

import interpreter.InterpreterIO

fun interface PrintEmitter {
    fun print(message: String)
}

fun interface InputProvider {
    fun readInput(prompt: String): String
}

fun interface EnvProvider {
    fun readEnv(key: String): String?
}

data class EngineIO(
    val emitter: PrintEmitter,
    val provider: InputProvider,
    val envProvider: EnvProvider = EnvProvider { null },
)

internal fun toInterpreterIO(io: EngineIO): InterpreterIO =
    InterpreterIO(
        emitter = { io.emitter.print(it) },
        provider = { io.provider.readInput(it) },
        envProvider = { io.envProvider.readEnv(it) },
    )
