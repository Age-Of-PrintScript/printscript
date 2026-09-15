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
        fun create(psVersion: String): Formatter = FormatterExecutor(psVersion)
    }
}

internal class FormatterExecutor(
    val psVersion: String,
) : Formatter {
    override fun execute(
        file: File,
        configPath: String,
    ): FormatResult<String, String> {
        val psVersionConfig =
            PSVersion.getVersion(psVersion).getOrReturn { return FormatError("Unknown version") }

        val source = file.readText()

        val lexer = Lexer.new(Lexicon(psVersionConfig.symbols, psVersionConfig.keywords))
        val parser = Parser.new(psVersionConfig.statementParsers)

        val tokens = lexer.tokenize(source).getOrReturn { return FormatError(it.getMessage()) }
        val program = parser.parse(tokens).getOrReturn { return FormatError(it.getMessage()) }

        val defaultConfig = ConfigProvider.defaultFor(psVersion).getOrReturn { return FormatError(it.getMessage()) }
        val providedConfig = applyJsonConfig(defaultConfig, File(configPath)).getOrReturn { return FormatError(it.getMessage()) }

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
        val tokenizer = config.tokenizers.find(ast) ?: return Failure(FormattingError.UNKNOWN_AST_TYPE)
        return Success(FormatterImplementation(config.rules, tokenizer))
    }
}
