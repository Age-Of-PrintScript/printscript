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
import tokens.ASSIGN
import tokens.Token

internal data class AssignmentIdSeen(
    val id: ASTIdentifier,
) : State {
    override fun consume(
        token: Token,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (token.type) {
            ASSIGN -> Success(ExpressionPending(id) to builder)
            else -> Failure(SyntaxError.MISSING_ASSIGNMENT_OPERATOR)
        }
}
