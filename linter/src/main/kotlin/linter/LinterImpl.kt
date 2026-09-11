package linter

import domain.Position
import domain.getOrReturn
import lexer.Lexer
import lexer.Lexicon
import parser.Parser
import versionfactory.PSVersion
import java.io.File
import java.io.InputStream

interface Linter {
    fun analyse(
        source: String,
        version: String? = null,
    ): List<Warning>

    companion object {
        fun createDefault(): Linter {
            val config = ConfigParser().parseDefault()
            return LinterImpl(config)
        }

        fun fromConfig(inputStream: InputStream): Linter {
            val config = ConfigParser().parse(inputStream)
            return LinterImpl(config)
        }

        fun fromConfigFile(file: File): Linter = fromConfig(file.inputStream())

        fun fromJson(jsonContent: String): Linter {
            val config = ConfigParser().parse(jsonContent)
            return LinterImpl(config)
        }

        internal fun fromRules(rulesConfig: RulesConfig): Linter = LinterImpl(rulesConfig)
    }
}

internal class LinterImpl(
    private val rulesConfig: RulesConfig,
    private val analyser: Analyser = Analyser(),
) : Linter {
    override fun analyse(
        source: String,
        version: String?,
    ): List<Warning> {
        val psVersion =
            if (version != null) {
                PSVersion.getVersion(version).getOrReturn {
                    return listOf(Warning(it.message, Position(0, 0)))
                }
            } else {
                PSVersion.getLatestVersion()
            }

        val lexer: Lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
        val parser: Parser = Parser.new(psVersion.statementParsers)

        val tokens = lexer.tokenize(source).getOrReturn { return listOf(Warning.fromError(it)) }
        val program = parser.parse(tokens).getOrReturn { return listOf(Warning.fromError(it)) }
        return analyser.analyse(program, rulesConfig)
    }
}
