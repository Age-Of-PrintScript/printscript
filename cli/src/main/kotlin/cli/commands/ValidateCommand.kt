package cli.commands

import cli.adapters.ConsoleLogger
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.types.file
import engine.Engine
import engine.Logger

class ValidateCommand(
    private val engine: Engine = Engine(),
    private val logger: Logger = ConsoleLogger(),
) : CliktCommand(name = "validate", help = "Valida que un script PrintScript este bien escrito") {
    private val file by argument()
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeWritable = false, mustBeReadable = true)
        .help("Ruta al archivo .ps a validar")

    override fun run() {
        val source = file.readText()
        engine.validate(source, logger)
    }
}
