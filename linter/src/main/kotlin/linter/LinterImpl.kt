package linter

import domain.getOrReturn
import lexer.Lexer
import parser.Parser
import java.io.File
import java.io.InputStream

interface Linter {
    fun analyse(source: String): List<Warning>

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

        fun fromRules(rulesConfig: RulesConfig): Linter = LinterImpl(rulesConfig)
    }
}

class LinterImpl(
    private val rulesConfig: RulesConfig,
    private val lexer: Lexer = Lexer.new(),
    private val parser: Parser = Parser.new(),
    private val analyser: Analyser = Analyser(),
) : Linter {
    override fun analyse(source: String): List<Warning> {
        val tokens = lexer.tokenize(source).getOrReturn { return listOf(Warning.fromError(it)) }
        val program = parser.parse(tokens).getOrReturn { return listOf(Warning.fromError(it)) }
        return analyser.analyse(program, rulesConfig)
    }
}
