package parser

import Either
import ast.Program
import tokens.Token

interface Parser {
    fun parse(tokens: List<Token>): Either<ParsingError, Program>
}
class ParserImpl : Parser {
    private val expressionParser = ExpressionParser()
    override fun parse(tokens: List<Token>): Either<ParsingError, Program> {
        val stateMachine = ParserStateMachine()
        val result = stateMachine.parse(tokens, expressionParser)
        return result
    }
}













