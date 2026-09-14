package cli.adapters

import engine.EngineIO
import engine.InputProvider
import engine.PrintEmitter

object ConsolePrinter : PrintEmitter {
    override fun print(message: String) {
        println(message)
    }
}

object ConsoleInput : InputProvider {
    override fun readInput(prompt: String): String {
        print(prompt)
        return readln()
    }
}

val engineIO: EngineIO =
    EngineIO(
        ConsolePrinter,
        ConsoleInput,
    )
