package formatter

import ast.AST
import domain.getOrReturn
import lexer.Lexer
import parser.Parser

interface Formatter {
    fun format(
        sourcePath: String,
        path: String,
        fileReader: FileReader,
    ): FormatResult

    companion object {
        fun new(): Formatter = FormatterExecutor()
    }
}

internal class FormatterExecutor : Formatter {
    private val lexer = Lexer.new()
    private val parser = Parser.new()

    override fun format(
        sourcePath: String,
        path: String,
        fileReader: FileReader,
    ): FormatResult {
        val source = fileReader.readText(sourcePath)

        val tokens = lexer.tokenize(source).getOrReturn { return FormatResult.Failure(it.getMessage()) }
        val program = parser.parse(tokens).getOrReturn { return FormatResult.Failure(it.getMessage()) }
        val config = parseConfig(path).getOrReturn { return FormatResult.Failure(it.getMessage()) }
        val formatters = FormatterFactory(config).constructFormatters().getOrReturn { return FormatResult.Failure(it.getMessage()) }

        var finalString = ""
        for (tree in program.trees) {
            val formatter =
                when (tree) { // el map.get siempre devuelve un nullable, tengo que manejar ese caso tambien
                    is AST.Declaration -> formatters["declaration"]
                    is AST.Assignment -> formatters["assignment"]
                    is AST.Call -> formatters["call"]
                } ?: return FormatResult.Failure(FormattingError.UNKNOWN_AST_TYPE.getMessage())
            finalString += formatter.format(tree) + "\n"
        }

        return FormatResult.Success(finalString)
    }
}
