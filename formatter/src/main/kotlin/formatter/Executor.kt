package formatter

import ast.AST
import domain.Either
import domain.Error
import domain.Failure
import domain.Success
import domain.getOrReturn
import lexer.Lexer
import lexer.Lexicon
import parser.Parser
import versionfactory.PSVersion
import java.io.File

class Executor(
    val config: ConfigProvider,
    val psVersion: PSVersion,
) {
    fun execute(file: File): Either<Error, String> {
        val source = file.readText()

        val lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
        val parser = Parser.new(psVersion.statementParsers)

        val tokens = lexer.tokenize(source).getOrReturn { return Failure(it) }
        val program = parser.parse(tokens).getOrReturn { return Failure(it) }

        val result = StringBuilder()
        for (ast in program.trees) {
            val formatter = formatterFor(ast).getOrReturn { return Failure(it) }
            val piece = formatter.format(ast).getOrReturn { return Failure(it) }
            result.append(piece)
        }
        return Success(result.toString())
    }

    private fun formatterFor(ast: AST): Either<Error, FormatterImplementation> {
        val entry =
            config.ruleSet.entries.firstOrNull { (tokenizer, _) -> tokenizer.tokenize(ast) is Success }
                ?: return Failure(FormattingError.UNKNOWN_AST_TYPE) // hardcodeado
        return Success(FormatterImplementation(entry.value, entry.key))
    }
}
