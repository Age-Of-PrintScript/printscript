package parser.states.assignment_branch

import ast.ASTIdentifier
import domain.Either
import domain.Failure
import domain.Success
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import parser.states.ConsumeResult
import parser.states.State
import parser.states.StatementComplete
import tokens.CLOSED_PARENTHESISViejo
import tokens.IdentifierViejo
import tokens.LiteralViejo
import tokens.OPEN_PARENTHESISViejo
import tokens.OperatorViejo
import tokens.SEMICOLONViejo
import tokens.TokenViejo

internal data class ExpressionPending(
    val id: ASTIdentifier,
    val tokenViejos: List<TokenViejo> = emptyList(),
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (tokenViejo.type) {
            SEMICOLONViejo -> {
                buildExpressionOnAst(expressionParser, builder)
            }
            is LiteralViejo, is IdentifierViejo, is OperatorViejo, is OPEN_PARENTHESISViejo, is CLOSED_PARENTHESISViejo ->
                Success(copy(tokenViejos = tokenViejos + tokenViejo) to builder)
            else -> Failure(SyntaxError.INVALID_TOKEN)
        }

    private fun buildExpressionOnAst(
        expressionParser: ExpressionParser,
        builder: ASTBuilder,
    ): Either<SyntaxError, ConsumeResult> =
        when (val result = expressionParser.parseExpression(tokenViejos)) {
            is Failure -> Failure(result.value)
            is Success -> {
                val newBuilder = builder.addExpression(result.value)
                Success(StatementComplete to newBuilder)
            }
        }
}
