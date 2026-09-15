package cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import formatter.ConfigProvider
import formatter.FormatError
import formatter.FormatSuccess
import formatter.Formatter
import java.io.File
import java.io.IOException

class Format : CliktCommand(name = "format", help = "Formatea un script PrintScript siguiendo las convenciones de codigo") {
    private val file by argument()
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeWritable = true, mustBeReadable = true)
        .help("Ruta al archivo .ps a formatear")

    private val config by option("-c", "--config", help = "Ruta al archivo de configuración JSON del formatter")
        .file(mustExist = true, canBeFile = true, canBeDir = false, mustBeReadable = true)

    private val version by option("-v", "--version", help = "Versión de PrintScript (1.0 o 1.1)")

    override fun run() {
        val formatVersion = version ?: "1.0"
        val configProvider =
            try {
                ConfigProvider.defaultFor(formatVersion)
            } catch (e: IllegalArgumentException) {
                System.err.println("Error initializing formatter: ${e.message}")
                return
            } catch (e: IllegalStateException) {
                System.err.println("Error initializing formatter: ${e.message}")
                return
            }

        val formatter = Formatter.new(configProvider, formatVersion)

        var tempConfigFile: File? = null
        val configPath =
            try {
                if (config != null) {
                    config!!.absolutePath
                } else {
                    val temp = File.createTempFile("ps_formatter_config", ".json")
                    val resourceStream = javaClass.classLoader.getResourceAsStream("format.config.json")
                    if (resourceStream != null) {
                        temp.writeBytes(resourceStream.readBytes())
                    } else {
                        temp.writeText("{}")
                    }
                    temp.deleteOnExit()
                    tempConfigFile = temp
                    temp.absolutePath
                }
            } catch (e: IOException) {
                System.err.println("Error accessing configuration file: ${e.message}")
                return
            }

        try {
            val result = formatter.execute(file, configPath)
            when (result) {
                is FormatSuccess -> {
                    file.writeText(result.value)
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
            tempConfigFile?.delete()
        }
    }
}
