package cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import formatter.FormatError
import formatter.FormatSuccess
import formatter.Formatter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class Format : CliktCommand(name = "format", help = "Formatea un script PrintScript siguiendo las convenciones de codigo") {
    private val file by argument()
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeWritable = true, mustBeReadable = true)
        .help("Ruta al archivo .ps a formatear")

    private val config by option("-c", "--config", help = "Ruta al archivo de configuración JSON del formatter")
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeReadable = true)

    private val version by option("-v", "--version", help = "Versión de PrintScript (1.0 o 1.1)")

    override fun run() {
        val formatVersion = version ?: "1.0"
        val configJson =
            try {
                config
                    ?.readText()
                    ?: javaClass
                        .classLoader
                        .getResourceAsStream("format.config.json")
                        ?.bufferedReader()
                        ?.use { it.readText() }
                    ?: "{}"
            } catch (e: IOException) {
                System.err.println("Error accessing configuration file: ${e.message}")
                return
            }
        val formatter = Formatter.create()

        val targetPath = file.toPath().toAbsolutePath()
        val tempPath = Files.createTempFile(targetPath.parent, file.name, ".tmp")
        try {
            val result =
                file.bufferedReader().use { reader ->
                    Files.newBufferedWriter(tempPath).use { writer ->
                        formatter.format(reader, writer, formatVersion, configJson)
                    }
                }
            when (result) {
                is FormatSuccess -> {
                    Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING)
                    println("File formatted successfully")
                }
                is FormatError -> {
                    System.err.println("Error formatting file: ${result.value}")
                }
            }
        } catch (e: IOException) {
            System.err.println("Error formatting file: ${e.message}")
        } catch (e: IllegalArgumentException) {
            System.err.println("Error formatting file: ${e.message}")
        } catch (e: IllegalStateException) {
            System.err.println("Error formatting file: ${e.message}")
        } finally {
            Files.deleteIfExists(tempPath)
        }
    }
}
