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
    fun format(
        source: String,
        version: String,
        configJson: String? = null,
    ): FormatResult<String, String>

    @Deprecated("Use format(source, version, configJson) instead")
    fun execute(
        file: File,
        configPath: String,
    ): FormatResult<String, String>

    companion object {
        fun create(): Formatter = FormatterExecutor()

        @Deprecated("Use create().format(source, version, configJson) instead")
        fun new(
            config: ConfigProvider,
            psVersion: String,
        ): Formatter = FormatterExecutor(config, psVersion)
    }
}

class FormatterExecutor(
    private val config: ConfigProvider? = null,
    private val psVersion: String? = null,
) : Formatter {
    override fun format(
        source: String,
        version: String,
        configJson: String?,
    ): FormatResult<String, String> {
        val resolvedVersion =
            PSVersion.getVersion(version).getOrReturn { return FormatError("Unknown version") }
        val resolvedConfig =
            resolveFormatterConfig(version, configJson).getOrReturn { return FormatError(it.getMessage()) }

        return formatSource(source, resolvedVersion, resolvedConfig)
    }

    @Deprecated("Use format(source, version, configJson) instead")
    override fun execute(
        file: File,
        configPath: String,
    ): FormatResult<String, String> {
        val legacyConfig = config ?: return FormatError("Legacy formatter configuration is missing")
        val legacyVersion = psVersion ?: return FormatError("Legacy formatter version is missing")
        val resolvedVersion =
            PSVersion.getVersion(legacyVersion).getOrReturn { return FormatError("Unknown version") }
        val providedConfig =
            applyJsonConfig(legacyConfig, File(configPath)).getOrReturn { return FormatError(it.getMessage()) }

        val source = file.readText()

        return formatSource(source, resolvedVersion, providedConfig)
    }

    private fun formatSource(
        source: String,
        version: PSVersion,
        config: ConfigProvider,
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
        config: ConfigProvider,
    ): Either<Error, FormatterImplementation> {
        val entry =
            config.ruleSet.entries.firstOrNull { (tokenizer, _) -> tokenizer.tokenize(ast) is Success }
                ?: return Failure(FormattingError.UNKNOWN_AST_TYPE)
        return Success(FormatterImplementation(entry.value, entry.key))
    }
}
