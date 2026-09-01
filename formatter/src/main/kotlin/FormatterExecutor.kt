import ast.AST
import domain.Either
import domain.Error
import domain.Failure
import domain.Success
import domain.getOrReturn
import lexer.Lexer
import lexer.LexerImpl
import parser.Parser
import parser.ParserImpl
import java.io.File

class FormatterExecutor {
    private val lexer: Lexer = LexerImpl()
    private val parser: Parser = ParserImpl()

    fun format(
        sourcePath: String,
        path: String,
    ): Either<Error, File> {
        val source = File(sourcePath).readText()

        val tokens = lexer.tokenize(source).getOrReturn { return Failure(it) }
        val program = parser.parse(tokens).getOrReturn { return Failure(it) }
        val config = parseConfig(path).getOrReturn { return Failure(it) }

        val formatters = FormatFactory(config).create()

        var finalString = ""
        for (tree in program.trees) {
            val formatter =
                when (tree) { // el map.get siempre devuelve un nullable, tengo que manejar ese caso tambien
                    is AST.Declaration -> formatters["declaration"]
                    is AST.Assignment -> formatters["assignment"]
                    is AST.Call -> formatters["call"]
                } ?: return Failure(FormattingError.UNKNOWN_AST_TYPE)
            finalString += formatter.format(tree) + "\n"
        }

        val outputFile = File(sourcePath)
        outputFile.writeText(finalString)
        return Success(outputFile)
    }
}
