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
import tokens.DataType
import tokens.Token

internal data class DeclarationColonSeen(val id: ASTIdentifier) : State {
    override fun consume(token: Token, builder: ASTBuilder, expressionParser: ExpressionParser): Either<SyntaxError, ConsumeResult> {
        return when (val t = token.type) {
            is DataType -> {
                val type = ASTDataType(t.type)
                Success(DeclarationTypeSeen(id, type) to builder.copy(dataType = type))
            }
            else -> Failure(SyntaxError.MISSING_TYPE_IN_DECLARATION)
        }
    }
}