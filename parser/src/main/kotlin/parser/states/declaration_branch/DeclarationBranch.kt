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
import tokens.Identifier
import tokens.Token

internal object DeclarationBranch : State {
    override fun consume(
        token: Token,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (val t = token.type) {
            is Identifier -> {
                Success(DeclarationIdSeen(ASTIdentifier(t.name)) to builder.copy(id = ASTIdentifier(t.name)))
            }
            else -> Failure(SyntaxError.MISSING_IDENTIFIER)
        }
}
