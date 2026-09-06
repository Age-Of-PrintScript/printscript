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
import tokens.ASSIGNViejo
import tokens.TokenViejo

internal data class AssignmentIdSeen(
    val id: ASTIdentifier,
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (tokenViejo.type) {
            ASSIGNViejo -> Success(ExpressionPending(id) to builder)
            else -> Failure(SyntaxError.MISSING_ASSIGNMENT_OPERATOR)
        }
}
