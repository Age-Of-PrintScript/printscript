package cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import formatter.Formatter

class Format(
    private val formatter: Formatter = Formatter.new(),
) : CliktCommand(name = "format", help = "Formatea un script PrintScript siguiendo las convenciones de codigo") {
    private val file by argument()
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeWritable = false, mustBeReadable = true)
        .help("Ruta al archivo .ps a formattear")

    private val version by option("-v", "--version", help = "Versión de PrintScript")

    override fun run() {
        TODO("Not yet implemented")
        println(formatter)
        println(file)
        println(version)
    }
}
