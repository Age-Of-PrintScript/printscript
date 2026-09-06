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
import tokens.TokenViejo

internal data class DeclarationIdSeen(
    val id: ASTIdentifier,
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (tokenViejo.type) {
            COLON -> Success(DeclarationColonSeen(id) to builder)
            else -> Failure(SyntaxError.MISSING_COLON_IN_DECLARATION)
        }
}
