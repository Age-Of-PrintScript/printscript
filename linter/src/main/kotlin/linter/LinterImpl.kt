package linter

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

interface Linter {
    fun analyse(
        source: String,
        version: String,
        configJson: String? = null,
    ): List<Warning>

    fun analyse(
        reader: Reader,
        version: String,
        configJson: String? = null,
    ): Sequence<Warning>

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
    ): List<Warning> = analyse(StringReader(source), version, configJson).toList()

    override fun analyse(
        reader: Reader,
        version: String,
        configJson: String?,
    ): Sequence<Warning> = analyseRequest(reader, version, configJson, analyser)
}

private fun analyseRequest(
    reader: Reader,
    version: String,
    configJson: String?,
    analyser: Analyser,
): Sequence<Warning> {
    val psVersion =
        PSVersion.getVersion(version).getOrReturn {
            throw IllegalArgumentException("Unsupported version: $version")
        }
    val rulesConfig = ConfigParser().resolve(configJson, version)
    val lexer = Lexer.new(Lexicon(psVersion.symbols, psVersion.keywords))
    val parser = Parser.new(psVersion.statementParsers)

    return sequence {
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
                yield(Warning.fromError(lexerError!!))
                return@sequence
            }
            val statement =
                when (parseResult) {
                    null -> return@sequence
                    is Failure -> {
                        yield(Warning.fromError(parseResult.value))
                        return@sequence
                    }
                    is Success -> parseResult.value
                }
            yieldAll(analyser.analyseNode(statement, rulesConfig))
        }
    }
}
