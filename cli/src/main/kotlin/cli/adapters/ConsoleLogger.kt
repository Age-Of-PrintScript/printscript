package cli.adapters

import executor.Logger

class ConsoleLogger : Logger {
    override fun log(string: String) {
        println(string)
    }
}
