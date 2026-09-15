package cli

import cli.commands.Format
import cli.commands.Lint
import cli.commands.Run
import cli.commands.Validate
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class PrintScriptCli :
    CliktCommand(
        name = "printscript",
        help = "CLI para ejecutar, validar, analizar y formatear código PrintScript",
    ) {
    override fun run() = Unit
}

fun main(args: Array<String>) {
    PrintScriptCli()
        .subcommands(Run(), Validate(), Lint(), Format())
        .main(args)
}
