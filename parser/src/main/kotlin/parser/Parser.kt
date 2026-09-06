package parser

import ast.Program
import domain.Either
import tokens.TokenViejo

interface Parser {
    fun parse(tokenViejos: List<TokenViejo>): Either<SyntaxError, Program>

    companion object {
        fun new(): Parser = ParserImpl()
    }
}

internal class ParserImpl : Parser {
    private val expressionParser = ExpressionParser()

    override fun parse(tokenViejos: List<TokenViejo>): Either<SyntaxError, Program> {
        val stateMachine = ParserStateMachine()
        return stateMachine.parse(tokenViejos, expressionParser)
    }
}
