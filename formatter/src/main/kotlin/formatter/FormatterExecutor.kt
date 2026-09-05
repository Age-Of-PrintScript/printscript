package formatter

import ast.AST
import domain.getOrReturn
import lexer.Lexer
import lexer.LexerImpl
import parser.Parser
import parser.ParserImpl

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
    private val lexer: Lexer = LexerImpl()
    private val parser: Parser = ParserImpl()

    override fun format(
        sourcePath: String,
        path: String,
        fileReader: FileReader,
    ): FormatResult {
        val source = fileReader.readText(sourcePath)

        val tokens = lexer.tokenize(source).getOrReturn { return FormatResult.Failure(it.mensaje) }
        val program = parser.parse(tokens).getOrReturn { return FormatResult.Failure(it.message) }
        // TODO cambiar el toString() a getMessage cuando se mergee pr nr: #66
        val config = parseConfig(path).getOrReturn { return FormatResult.Failure(it.toString()) }
        // TODO cambiar el toString() a getMessage cuando se mergee pr nr: #66
        val formatters = FormatterFactory(config).constructFormatters().getOrReturn { return FormatResult.Failure(it.toString()) }

        var finalString = ""
        for (tree in program.trees) {
            val formatter =
                when (tree) { // el map.get siempre devuelve un nullable, tengo que manejar ese caso tambien
                    is AST.Declaration -> formatters["declaration"]
                    is AST.Assignment -> formatters["assignment"]
                    is AST.Call -> formatters["call"]
                    // TODO cambiar el toString() a getMessage cuando se mergee pr nr: #66
                } ?: return FormatResult.Failure(FormattingError.UNKNOWN_AST_TYPE.toString())
            finalString += formatter.format(tree) + "\n"
        }

        return FormatResult.Success(finalString)
    }
}
