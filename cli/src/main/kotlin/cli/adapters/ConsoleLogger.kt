package cli.adapters

import engine.Logger

class ConsoleLogger : Logger {
    override fun log(string: String) {
        println(string)
    }
}
