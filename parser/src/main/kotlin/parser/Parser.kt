package parser

import domain.Either
import ast.Program
import tokens.Token

interface Parser {
    fun parse(tokens: List<Token>): Either<SyntaxError, Program>
}
class ParserImpl : Parser {
    private val expressionParser = ExpressionParser()
    override fun parse(tokens: List<Token>): Either<SyntaxError, Program> {
        val stateMachine = ParserStateMachine()
        return stateMachine.parse(tokens, expressionParser)
    }
}
