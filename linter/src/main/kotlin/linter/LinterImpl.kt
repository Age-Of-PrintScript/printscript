package linter

import domain.getOrReturn
import lexer.Lexer
import lexer.Lexicon
import parser.Parser
import versionfactory.PSVersion
import java.io.InputStream

interface Linter {
    fun analyse(
        source: String,
        version: String,
        configJson: String? = null,
    ): List<Warning>

    @Deprecated("Use analyse(source, version, configJson) instead")
    fun analyse(source: String): List<Warning>

    companion object {
        fun create(): Linter = RequestLinter()

        @Deprecated("Use create().analyse(source, version, configJson) instead")
        fun createDefault(version: String = "1.0"): Linter {
            val config = ConfigParser().parseDefault(version)
            return fromRules(config, version)
        }

        @Deprecated("Use create().analyse(source, version, configJson) instead")
        fun fromConfig(
            inputStream: InputStream,
            version: String = "1.0",
        ): Linter {
            val config = ConfigParser().parse(inputStream, version)
            return fromRules(config, version)
        }

        @Deprecated("Use create().analyse(source, version, configJson) instead")
        fun fromJson(
            jsonContent: String,
            version: String = "1.0",
        ): Linter {
            val config = ConfigParser().parse(jsonContent, version)
            return fromRules(config, version)
        }

        @Deprecated("Use create().analyse(source, version, configJson) instead")
        internal fun fromRules(
            rulesConfig: RulesConfig,
            version: String = "1.0",
        ): Linter {
            val psVersion =
                PSVersion.getVersion(version).getOrReturn {
                    throw IllegalArgumentException("Unsupported version: $version")
                }
            val lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
            val parser = Parser.new(psVersion.statementParsers)
            return LinterImpl(
                rulesConfig,
                lexer = lexer,
                parser = parser,
            )
        }
    }
}

internal class RequestLinter : Linter {
    override fun analyse(
        source: String,
        version: String,
        configJson: String?,
    ): List<Warning> {
        val configuredLinter =
            configJson?.let { Linter.fromJson(it, version) }
                ?: Linter.createDefault(version)
        return configuredLinter.analyse(source)
    }

    @Deprecated("Use analyse(source, version, configJson) instead")
    override fun analyse(source: String): List<Warning> = throw UnsupportedOperationException("A PrintScript version is required to analyse source code")
}

internal class LinterImpl(
    private val rulesConfig: RulesConfig,
    private val lexer: Lexer,
    private val parser: Parser,
    private val analyser: Analyser = Analyser(),
) : Linter {
    override fun analyse(
        source: String,
        version: String,
        configJson: String?,
    ): List<Warning> = Linter.create().analyse(source, version, configJson)

    @Deprecated("Use analyse(source, version, configJson) instead")
    override fun analyse(source: String): List<Warning> {
        val tokens = lexer.tokenize(source).getOrReturn { return listOf(Warning.fromError(it)) }
        val program = parser.parse(tokens).getOrReturn { return listOf(Warning.fromError(it)) }
        return analyser.analyse(program, rulesConfig)
    }
}
