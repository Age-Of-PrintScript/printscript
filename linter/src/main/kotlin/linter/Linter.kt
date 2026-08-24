package linter

import domain.Either
import domain.Error
import domain.Failure
import domain.Success
import domain.getOrReturn
import lexer.Lexer
import lexer.LexerImpl
import parser.Parser
import parser.ParserImpl
import java.io.File

class Linter(configFile: File) {
    private val lexer: Lexer = LexerImpl()
    private val parser: Parser = ParserImpl()
    private val rulesConfig: RulesConfig = ConfigParser().parse(configFile)
    private val analyser = Analyser()

    fun analyse(source: String): Either<Error, List<Warning>> {
        val tokens = lexer.tokenize(source).getOrReturn { return Failure(it) }
        val program = parser.parse(tokens).getOrReturn { return Failure(it) }
        val analysis = analyser.analyse(program, rulesConfig)
        return Success(analysis)
    }
}