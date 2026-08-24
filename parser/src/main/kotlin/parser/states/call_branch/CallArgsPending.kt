package parser.states.call_branch

import domain.Either
import domain.Failure
import domain.PrintScriptFunctions
import domain.Success
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import parser.states.ConsumeResult
import parser.states.State
import tokens.CLOSED_PARENTHESIS
import tokens.Identifier
import tokens.Literal
import tokens.Operator
import tokens.Token

internal data class CallArgsPending(
    val function: PrintScriptFunctions,
    val tokens: List<Token> = emptyList(),
) : State {
    override fun consume(
        token: Token,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (token.type) {
            is Literal, is Identifier, is Operator ->
                Success(copy(tokens = tokens + token) to builder)
            is CLOSED_PARENTHESIS -> {
                when (val res = expressionParser.parseExpression(tokens)) {
                    is Failure -> Failure(res.value)
                    is Success -> {
                        val newBuilder = builder.addExpression(res.value)
                        Success(CallArgsClosed(function) to newBuilder)
                    }
                }
            }
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }
}
