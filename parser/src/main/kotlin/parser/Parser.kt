package parser

import ast.OldProgram
import domain.Either
import tokens.TokenViejo

interface Parser {
    fun parse(tokenViejos: List<TokenViejo>): Either<SyntaxError, OldProgram>

    companion object {
        fun new(): Parser = ParserImpl()
    }
}

internal class ParserImpl : Parser {
    private val oldExpressionParser = OldExpressionParser()

    override fun parse(tokenViejos: List<TokenViejo>): Either<SyntaxError, OldProgram> {
        val stateMachine = ParserStateMachine()
        return stateMachine.parse(tokenViejos, oldExpressionParser)
    }
}
