package formatter

import ast.AST
import domain.getOrReturn
import lexer.Lexer
import lexer.Lexicon
import parser.Parser
import versionfactory.PSVersion

interface Formatter {
    fun format(
        sourcePath: String,
        path: String,
        fileReader: FileReader,
        version: String? = null,
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
        version: String?,
    ): FormatResult {
        val psVersion =
            if (version != null) {
                PSVersion.getVersion(version).getOrReturn {
                    return FormatResult.Failure(it.message)
                }
            } else {
                PSVersion.getLatestVersion()
            }

        val lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
        val parser = Parser.new(psVersion.statementParsers)

        val source = fileReader.readText(sourcePath)

        val tokens = lexer.tokenize(source).getOrReturn { return FormatResult.Failure(it.getMessage()) }
        val program = parser.parse(tokens).getOrReturn { return FormatResult.Failure(it.getMessage()) }
        val config = parseConfig(path).getOrReturn { return FormatResult.Failure(it.getMessage()) }
        val formatters = FormatterFactory(config).constructFormatters().getOrReturn { return FormatResult.Failure(it.getMessage()) }

        val finalString =
            buildString {
                for (tree in program.trees) {
                    val formatter =
                        when (tree) {
                            is AST.Declaration -> formatters["declaration"]
                            is AST.Assignment -> formatters["assignment"]
                            is AST.ExpressionStatement -> formatters["expressionStatement"]
                            is AST.IfStatement -> formatters["ifStatement"]
                        } ?: return FormatResult.Failure(FormattingError.UNKNOWN_AST_TYPE.getMessage())
                    appendLine(formatter.format(tree))
                }
            }

        return FormatResult.Success(finalString)
    }
}
