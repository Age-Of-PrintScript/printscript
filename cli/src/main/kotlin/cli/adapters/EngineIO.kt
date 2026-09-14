package cli.adapters

import engine.EngineIO
import engine.EngineInputProvider
import engine.EnginePrintEmitter
import engine.EnvProvider

object ConsolePrinter : EnginePrintEmitter {
    override fun print(message: String) {
        println(message)
    }
}

object ConsoleInput : EngineInputProvider {
    override fun readInput(prompt: String): String {
        print(prompt)
        return readln()
    }
}

object ConsoleEnv : EnvProvider {
    override fun readEnv(key: String): String? = System.getenv(key)
}

val engineIO: EngineIO =
    EngineIO(
        ConsolePrinter,
        ConsoleInput,
        ConsoleEnv,
    )
