package cli

import cli.commands.RunCommand
import cli.commands.ValidateCommand
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class PrintScriptCli :
    CliktCommand(
        name = "printscript",
        help = "CLI para ejecutar y validar código PrintScript",
    ) {
    override fun run() = Unit
}

fun main(args: Array<String>) {
    PrintScriptCli()
        .subcommands(RunCommand(), ValidateCommand())
        .main(args)
}
