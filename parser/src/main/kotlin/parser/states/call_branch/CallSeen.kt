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
import tokens.TokenViejo

internal data class CallSeen(
    val function: PrintScriptFunctions,
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (tokenViejo.type) {
            OPEN_PARENTHESIS -> Success(CallArgsPending(function) to builder)
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }
}
