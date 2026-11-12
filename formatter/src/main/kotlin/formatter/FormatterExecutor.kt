package formatter

import ast.AST
import domain.Either
import domain.Failure
import domain.Success
import domain.getOrReturn
import lexer.Lexer
import lexer.LexerError
import lexer.Lexicon
import parser.Parser
import parser.tokenConsumers.TokenConsumer
import versionfactory.PSVersion
import java.io.Reader
import java.io.StringReader
import java.io.StringWriter
import java.io.Writer

interface Formatter {
    fun format(
        source: String,
        version: String,
        configJson: String? = null,
    ): FormatResult<String, String>

    fun format(
        reader: Reader,
        writer: Writer,
        version: String,
        configJson: String? = null,
    ): FormatResult<String, Unit>

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
        val writer = StringWriter()
        format(StringReader(source), writer, version, configJson)
            .getOrReturn { return FormatError(it) }
        return FormatSuccess(writer.toString())
    }

    override fun format(
        reader: Reader,
        writer: Writer,
        version: String,
        configJson: String?,
    ): FormatResult<String, Unit> {
        val resolvedVersion =
            PSVersion.getVersion(version).getOrReturn { return FormatError("Unknown version") }
        val resolvedConfig =
            resolveFormatterConfig(resolvedVersion, configJson).getOrReturn { return FormatError(it.getMessage()) }

        return formatSource(reader, writer, resolvedVersion, resolvedConfig)
    }

    private fun formatSource(
        reader: Reader,
        writer: Writer,
        version: PSVersion,
        config: ResolvedFormatterConfig,
    ): FormatResult<String, Unit> {
        val lexer = Lexer.new(Lexicon(version.symbols, version.keywords))
        val parser = Parser.new(version.statementParsers)

        var lexerError: LexerError? = null
        val consumer =
            TokenConsumer.from {
                if (lexerError != null) return@from null
                when (val result = lexer.nextToken(reader)) {
                    null -> null
                    is Success -> result.value
                    is Failure -> {
                        lexerError = result.value
                        null
                    }
                }
            }

        while (true) {
            val parseResult = parser.parseNext(consumer)
            if (lexerError != null) {
                return FormatError(lexerError!!.getMessage())
            }
            if (parseResult == null) {
                return FormatSuccess(Unit)
            }
            val ast = parseResult.getOrReturn { return FormatError(it.getMessage()) }
            val formatter = formatterFor(ast, config).getOrReturn { return FormatError(it.getMessage()) }
            val piece = formatter.format(ast).getOrReturn { return FormatError(it.getMessage()) }
            writer.write(piece)
        }
    }

    private fun formatterFor(
        ast: AST,
        config: ResolvedFormatterConfig,
    ): Either<FormattingError, FormatterImplementation> {
        val tokenizer =
            config.tokenizers.firstOrNull { it.supports(ast) }
                ?: return Failure(FormattingError.UNKNOWN_AST_TYPE)
        return Success(FormatterImplementation(config.rules, tokenizer))
    }
}
