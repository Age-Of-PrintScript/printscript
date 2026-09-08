package parser.states.declaration_branch

import ast.ASTIdentifier
import domain.Either
import domain.Failure
import domain.Success
import parser.ASTBuilder
import parser.OldExpressionParser
import parser.SyntaxError
import parser.states.ConsumeResult
import parser.states.State
import tokens.COLONViejo
import tokens.TokenViejo

internal data class DeclarationIdSeen(
    val id: ASTIdentifier,
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        oldExpressionParser: OldExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (tokenViejo.type) {
            COLONViejo -> Success(DeclarationColonSeen(id) to builder)
            else -> Failure(SyntaxError.MISSING_COLON_IN_DECLARATION)
        }
}
