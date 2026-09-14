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

interface Formatter {
    fun execute(
        file: File,
        configPath: String,
    ): FormatResult<String, String>

    companion object {
        fun new(
            config: ConfigProvider,
            psVersion: String,
        ) = FormatterExecutor(config, psVersion)
    }
}

class FormatterExecutor(
    val config: ConfigProvider,
    val psVersion: String,
) : Formatter {
    override fun execute(
        file: File,
        configPath: String,
    ): FormatResult<String, String> {
        val psVersion =
            PSVersion.getVersion(psVersion).getOrReturn { return FormatError("Unknown version") }

        val source = file.readText()

        val lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
        val parser = Parser.new(psVersion.statementParsers)

        val tokens = lexer.tokenize(source).getOrReturn { return FormatError(it.getMessage()) }
        val program = parser.parse(tokens).getOrReturn { return FormatError(it.getMessage()) }

        val providedConfig = applyJsonConfig(config, File(configPath)).getOrReturn { return FormatError(it.getMessage()) }

        val result = StringBuilder()
        for (ast in program.trees) {
            val formatter = formatterFor(ast, providedConfig).getOrReturn { return FormatError(it.getMessage()) }
            val piece = formatter.format(ast).getOrReturn { return FormatError(it.getMessage()) }
            result.append(piece)
        }
        return FormatSuccess(result.toString())
    }

    private fun formatterFor(
        ast: AST,
        config: ConfigProvider,
    ): Either<Error, FormatterImplementation> {
        val entry =
            config.ruleSet.entries.firstOrNull { (tokenizer, _) -> tokenizer.tokenize(ast) is Success }
                ?: return Failure(FormattingError.UNKNOWN_AST_TYPE)
        return Success(FormatterImplementation(entry.value, entry.key))
    }
}
