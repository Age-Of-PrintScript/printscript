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
import tokens.IdentifierViejo
import tokens.TokenViejo

internal object DeclarationBranch : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        oldExpressionParser: OldExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (val t = tokenViejo.type) {
            is IdentifierViejo -> {
                Success(DeclarationIdSeen(ASTIdentifier(t.name)) to builder.copy(id = ASTIdentifier(t.name)))
            }
            else -> Failure(SyntaxError.MISSING_IDENTIFIER)
        }
}
