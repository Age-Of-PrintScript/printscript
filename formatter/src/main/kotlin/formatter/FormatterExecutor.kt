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
    ): FormatResult

    companion object {
        fun new(): Formatter = FormatterExecutor()
    }
}

class FormatterExecutor : Formatter {
    private val lexer: Lexer = LexerImpl()
    private val parser: Parser = ParserImpl()
    private val fileReader: FileReader = FileReaderFake("content") // esto es un dummy, hable con Facu y despues se implementa con el CLI

    override fun format(
        sourcePath: String,
        path: String,
    ): FormatResult {
        val source = fileReader.readText(sourcePath)

        val tokens = lexer.tokenize(source).getOrReturn { return FormatResult.Failure(it) }
        val program = parser.parse(tokens).getOrReturn { return FormatResult.Failure(it) }
        val config = parseConfig(path).getOrReturn { return FormatResult.Failure(it) }

        val formatters = FormatterFactory(config).constructFormatters().getOrReturn { return FormatResult.Failure(it) }

        var finalString = ""
        for (tree in program.trees) {
            val formatter =
                when (tree) { // el map.get siempre devuelve un nullable, tengo que manejar ese caso tambien
                    is AST.Declaration -> formatters["declaration"]
                    is AST.Assignment -> formatters["assignment"]
                    is AST.Call -> formatters["call"]
                } ?: return FormatResult.Failure(FormattingError.UNKNOWN_AST_TYPE)
            finalString += formatter.format(tree) + "\n"
        }

        return FormatResult.Success(finalString)
    }
}
