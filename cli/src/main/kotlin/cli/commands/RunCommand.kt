package cli.commands

import cli.adapters.ConsoleLogger
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.types.file
import executor.Engine
import executor.Logger

class RunCommand(
    private val engine: Engine = Engine(),
    private val logger: Logger = ConsoleLogger(),
) : CliktCommand(name = "run", help = "Ejecuta un script PrintScript a partir de un archivo") {
    private val file by argument()
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeWritable = false, mustBeReadable = true)
        .help("Ruta al archivo .ps a ejecutar")

    override fun run() {
        val source = file.readText()
        val execution = engine.execute(source, logger)
        println(execution)
    }
}
