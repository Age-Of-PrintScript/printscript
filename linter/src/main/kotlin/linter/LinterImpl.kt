package linter

import domain.getOrReturn
import lexer.Lexer
import lexer.Lexicon
import parser.Parser
import versionfactory.PSVersion

interface Linter {
    fun analyse(
        source: String,
        version: String,
        configJson: String? = null,
    ): List<Warning>

    companion object {
        fun create(): Linter = LinterImpl()
    }
}

internal class LinterImpl(
    private val analyser: Analyser = Analyser(),
) : Linter {
    override fun analyse(
        source: String,
        version: String,
        configJson: String?,
    ): List<Warning> = analyseRequest(source, version, configJson, analyser)
}

private fun analyseRequest(
    source: String,
    version: String,
    configJson: String?,
    analyser: Analyser,
): List<Warning> {
    val psVersion =
        PSVersion.getVersion(version).getOrReturn {
            throw IllegalArgumentException("Unsupported version: $version")
        }
    val rulesConfig = ConfigParser().resolve(configJson, version)
    val lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
    val parser = Parser.new(psVersion.statementParsers)

    val tokens = lexer.tokenize(source).getOrReturn { return listOf(Warning.fromError(it)) }
    val program = parser.parse(tokens).getOrReturn { return listOf(Warning.fromError(it)) }
    return analyser.analyse(program, rulesConfig)
}
