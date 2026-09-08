package parser.states.declaration_branch

import ast.ASTDataType
import ast.ASTIdentifier
import domain.Either
import domain.Failure
import domain.Success
import parser.ASTBuilder
import parser.OldExpressionParser
import parser.SyntaxError
import parser.states.ConsumeResult
import parser.states.State
import parser.states.StatementComplete
import parser.states.assignment_branch.ExpressionPending
import tokens.ASSIGNViejo
import tokens.SEMICOLONViejo
import tokens.TokenViejo

internal data class DeclarationTypeSeen(
    val id: ASTIdentifier,
    val type: ASTDataType,
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        oldExpressionParser: OldExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (tokenViejo.type) {
            ASSIGNViejo -> Success(ExpressionPending(id) to builder)
            SEMICOLONViejo -> Success(StatementComplete to builder)
            else -> Failure(SyntaxError.INVALID_TOKEN_AFTER_TYPE)
        }
}
