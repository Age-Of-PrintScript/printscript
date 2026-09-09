package formatter

import ast.AST
import domain.getOrReturn
import lexer.Lexer
import lexer.Lexicon
import parser.Parser
import versionfactory.version1_0

interface Formatter {
    fun format(
        sourcePath: String,
        path: String,
        fileReader: FileReader,
        version: String,
    ): FormatResult

    companion object {
        fun new(): Formatter = FormatterExecutor()
    }
}

internal class FormatterExecutor : Formatter {
    override fun format(
        sourcePath: String,
        path: String,
        fileReader: FileReader,
        version: String,
    ): FormatResult {
        val psVersion = version1_0

        val lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
        val parser = Parser.new(psVersion.statementParsers)

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
                    is AST.ExpressionStatement -> formatters["call"]
                } ?: return FormatResult.Failure(FormattingError.UNKNOWN_AST_TYPE.getMessage())
            finalString += formatter.format(tree) + "\n"
        }

        return FormatResult.Success(finalString)
    }
}
