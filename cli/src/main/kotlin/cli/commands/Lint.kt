package cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import linter.Linter

class Lint : CliktCommand(name = "lint", help = "Analiza un script PrintScript siguiendo las convenciones de codigo") {
    private val file by argument()
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeWritable = false, mustBeReadable = true)
        .help("Ruta al archivo .ps a analizar")

    private val config by option("-c", "--config", help = "Ruta al archivo de configuración JSON del linter")
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeReadable = true)

    private val version by option("-v", "--version", help = "Versión de PrintScript (1.0 o 1.1)")

    override fun run() {
        val linterVersion = version ?: "1.0"
        val linter =
            // los catch feos son porque detekt se queja
            try {
                config?.let { Linter.fromConfig(it.inputStream(), version = linterVersion) }
                    ?: Linter.createDefault(version = linterVersion)
            } catch (e: IllegalArgumentException) {
                System.err.println("Error initializing linter: ${e.message}")
                return
            } catch (e: IllegalStateException) {
                System.err.println("Error initializing linter: ${e.message}")
                return
            } catch (e: java.io.IOException) {
                System.err.println("Error initializing linter: ${e.message}")
                return
            }

        val source = file.readText()
        val warnings = linter.analyse(source)
        if (warnings.isNotEmpty()) {
            println(warnings.joinToString("\n"))
        } else {
            println("No warnings found")
        }
    }
}
