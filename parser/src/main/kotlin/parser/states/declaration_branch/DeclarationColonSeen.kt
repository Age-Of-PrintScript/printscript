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
import tokens.DataTypeViejo
import tokens.TokenViejo

internal data class DeclarationColonSeen(
    val id: ASTIdentifier,
) : State {
    override fun consume(
        tokenViejo: TokenViejo,
        builder: ASTBuilder,
        expressionParser: ExpressionParser,
    ): Either<SyntaxError, ConsumeResult> =
        when (val t = tokenViejo.type) {
            is DataTypeViejo -> {
                val type = ASTDataType(t.type)
                Success(DeclarationTypeSeen(id, type) to builder.copy(dataType = type))
            }
            else -> Failure(SyntaxError.MISSING_TYPE_IN_DECLARATION)
        }
}
