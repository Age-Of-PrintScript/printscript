package engine

import interpreter.InterpreterIO

fun interface PrintEmitter {
    fun print(message: String)
}

fun interface InputProvider {
    fun readInput(prompt: String): String
}

data class EngineIO(
    val emitter: PrintEmitter,
    val provider: InputProvider,
)

internal fun toInterpreterIO(io: EngineIO): InterpreterIO =
    InterpreterIO(
        emitter = { io.emitter.print(it) },
        provider = { io.provider.readInput(it) },
    )
