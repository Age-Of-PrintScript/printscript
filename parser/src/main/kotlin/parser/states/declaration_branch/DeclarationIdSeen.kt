package parser.states.declaration_branch

import ast.ASTIdentifier
import domain.Either
import domain.Failure
import domain.Success
import parser.ASTBuilder
import parser.ExpressionParser
import parser.SyntaxError
import parser.states.ConsumeResult
import parser.states.State
import tokens.COLON
import tokens.Token

internal data class DeclarationIdSeen(val id: ASTIdentifier) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (token.type) {
            COLON -> Success(DeclarationColonSeen(id) to builder)
            else -> Failure(SyntaxError.MISSING_COLON_IN_DECLARATION)
        }
    }
}