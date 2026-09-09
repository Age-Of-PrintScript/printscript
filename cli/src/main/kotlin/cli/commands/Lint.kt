package cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import linter.Linter

class Lint(
    private val linter: Linter = Linter.createDefault(),
) : CliktCommand(name = "lint", help = "Analiza un script PrintScript siguiendo las convenciones de codigo") {
    private val file by argument()
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeWritable = false, mustBeReadable = true)
        .help("Ruta al archivo .ps a analizar")

    private val version by option("-v", "--version", help = "Versión de PrintScript")

    override fun run() {
        val source = file.readText()
        val warnings = linter.analyse(source, version = version)
        if (warnings.isNotEmpty()) {
            println(warnings.joinToString("\n"))
        } else {
            println("No warnings found")
        }
    }
}
