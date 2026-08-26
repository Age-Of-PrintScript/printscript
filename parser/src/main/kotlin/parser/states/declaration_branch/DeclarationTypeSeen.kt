package parser.states.declaration_branch

import ast.ASTDataType
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
import parser.states.assignment_branch.ExpressionPending
import tokens.ASSIGN
import tokens.SEMICOLON
import tokens.Token

internal data class DeclarationTypeSeen(
    val id: ASTIdentifier,
    val type: ASTDataType,
) : State {
    override fun consume(
        token: Token,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (token.type) {
            ASSIGN -> Success(ExpressionPending(id) to builder)
            SEMICOLON -> Success(StatementComplete to builder)
            else -> Failure(SyntaxError.INVALID_TOKEN_AFTER_TYPE)
        }
}
