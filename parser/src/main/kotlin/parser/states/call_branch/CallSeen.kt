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
import tokens.OPEN_PARENTHESIS
import tokens.Token

internal data class CallSeen(
    val function: PrintScriptFunctions,
) : State {
    override fun consume(
        token: Token,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (token.type) {
            OPEN_PARENTHESIS -> Success(CallArgsPending(function) to builder)
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }
}
