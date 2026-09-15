package engine

import interpreter.InterpreterIO

fun interface EnginePrintEmitter {
    fun print(message: String)
}

fun interface EngineInputProvider {
    fun readInput(prompt: String): String
}

fun interface EnvProvider {
    fun readEnv(key: String): String?
}

data class EngineIO(
    val emitter: EnginePrintEmitter,
    val provider: EngineInputProvider,
    val envProvider: EnvProvider = EnvProvider { null },
)

internal fun toInterpreterIO(io: EngineIO): InterpreterIO =
    InterpreterIO(
        emitter = { io.emitter.print(it) },
        provider = { io.provider.readInput(it) },
        envProvider = { io.envProvider.readEnv(it) },
    )
