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

interface Formatter {
    fun format(
        source: String,
        version: String,
        configJson: String? = null,
    ): FormatResult<String, String>

    companion object {
        fun create(): Formatter = FormatterExecutor()
    }
}

class FormatterExecutor : Formatter {
    override fun format(
        source: String,
        version: String,
        configJson: String?,
    ): FormatResult<String, String> {
        val resolvedVersion =
            PSVersion.getVersion(version).getOrReturn { return FormatError("Unknown version") }
        val resolvedConfig =
            resolveFormatterConfig(resolvedVersion, configJson).getOrReturn { return FormatError(it.getMessage()) }

        return formatSource(source, resolvedVersion, resolvedConfig)
    }

    private fun formatSource(
        source: String,
        version: PSVersion,
        config: ResolvedFormatterConfig,
    ): FormatResult<String, String> {
        val lexer = Lexer.new(Lexicon(version.symbols, version.keywords))
        val parser = Parser.new(version.statementParsers)

        val tokens = lexer.tokenize(source).getOrReturn { return FormatError(it.getMessage()) }
        val program = parser.parse(tokens).getOrReturn { return FormatError(it.getMessage()) }

        val result = StringBuilder()
        for (ast in program.trees) {
            val formatter = formatterFor(ast, config).getOrReturn { return FormatError(it.getMessage()) }
            val piece = formatter.format(ast).getOrReturn { return FormatError(it.getMessage()) }
            result.append(piece)
        }
        return FormatSuccess(result.toString())
    }

    private fun formatterFor(
        ast: AST,
        config: ResolvedFormatterConfig,
    ): Either<Error, FormatterImplementation> {
        val tokenizer =
            config.tokenizers.firstOrNull { it.supports(ast) }
                ?: return Failure(FormattingError.UNKNOWN_AST_TYPE)
        return Success(FormatterImplementation(config.rules, tokenizer))
    }
}
