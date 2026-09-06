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
import parser.states.StatementComplete
import tokens.SEMICOLON
import tokens.TokenViejo

internal data class CallArgsClosed(
    val function: PrintScriptFunctions,
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (tokenViejo.type) {
            SEMICOLON -> Success(StatementComplete to builder)
            else -> Failure(SyntaxError.UNEXPECTED_TOKEN_AFTER_STATEMENT)
        }
}
