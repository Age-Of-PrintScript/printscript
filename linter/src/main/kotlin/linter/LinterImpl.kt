package linter

import domain.getOrReturn
import lexer.Lexer
import lexer.Lexicon
import parser.Parser
import versionfactory.PSVersion
import java.io.InputStream

interface Linter {
    val version: String

    fun analyse(source: String): List<Warning>

    companion object {
        fun createDefault(version: String = "1.0"): Linter {
            val config = ConfigParser().parseDefault(version)
            return fromRules(config, version)
        }

        fun fromConfig(
            inputStream: InputStream,
            version: String = "1.0",
        ): Linter {
            val config = ConfigParser().parse(inputStream, version)
            return fromRules(config, version)
        }

        fun fromJson(
            jsonContent: String,
            version: String = "1.0",
        ): Linter {
            val config = ConfigParser().parse(jsonContent, version)
            return fromRules(config, version)
        }

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
                version,
                rulesConfig,
                lexer = lexer,
                parser = parser,
            )
        }
    }
}

internal class LinterImpl(
    override val version: String,
    private val rulesConfig: RulesConfig,
    private val lexer: Lexer,
    private val parser: Parser,
    private val analyser: Analyser = Analyser(),
) : Linter {
    override fun analyse(source: String): List<Warning> {
        val tokens = lexer.tokenize(source).getOrReturn { return listOf(Warning.fromError(it)) }
        val program = parser.parse(tokens).getOrReturn { return listOf(Warning.fromError(it)) }
        return analyser.analyse(program, rulesConfig)
    }
}
