package parser.states.call_branch

import domain.Either
import domain.Failure
import domain.PrintScriptFunctions
import domain.Success
import parser.ASTBuilder
import parser.OldExpressionParser
import parser.SyntaxError
import parser.states.ConsumeResult
import parser.states.State
import tokens.CLOSED_PARENTHESISViejo
import tokens.IdentifierViejo
import tokens.LiteralViejo
import tokens.OperatorViejo
import tokens.TokenViejo

internal data class CallArgsPending(
    val function: PrintScriptFunctions,
    val tokenViejos: List<TokenViejo> = emptyList(),
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        oldExpressionParser: OldExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (tokenViejo.type) {
            is LiteralViejo, is IdentifierViejo, is OperatorViejo ->
                Success(copy(tokenViejos = tokenViejos + tokenViejo) to builder)
            is CLOSED_PARENTHESISViejo -> {
                when (val res = oldExpressionParser.parseExpression(tokenViejos)) {
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
