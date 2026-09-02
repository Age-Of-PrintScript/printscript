package parser

import ast.Program
import domain.Either
import tokens.Token

interface Parser {
    fun parse(tokens: List<Token>): Either<SyntaxError, Program>

    companion object {
        fun new(): Parser = ParserImpl()
    }
}

internal class ParserImpl : Parser {
    private val expressionParser = ExpressionParser()

    override fun parse(tokens: List<Token>): Either<SyntaxError, Program> {
        val stateMachine = ParserStateMachine()
        return stateMachine.parse(tokens, expressionParser)
    }
}
