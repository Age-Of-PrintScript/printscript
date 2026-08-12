package parser

import domain.Either
import domain.Failure
import domain.PrintScriptFunctions
import domain.Success
import ast.AST
import ast.ASTDataType
import ast.ASTIdentifier
import ast.Expression

internal sealed interface ASTBuilder {
    fun build(): Either<SyntaxError, AST>
    object EmptyBuilder : ASTBuilder{
        override fun build(): Either<SyntaxError, AST> {
            return Failure(SyntaxError.INVALID_TOKEN)
        }
    }

    data class DeclarationBuilder(
        val id: ASTIdentifier? = null,
        val type: ASTDataType? = null,
        val value: Expression? = null
    ) : ASTBuilder {
        override fun build(): Either<SyntaxError, AST> {
            val safeId = id ?: return Failure(SyntaxError.MISSING_IDENTIFIER)
            val safeType = type ?: return Failure(SyntaxError.MISSING_TYPE_IN_DECLARATION)
            return Success(AST.Declaration(safeId, safeType, value))
        }
    }

    data class AssignmentBuilder(
        val id: ASTIdentifier? = null,
        val value: Expression? = null
    ) : ASTBuilder {
        override fun build(): Either<SyntaxError, AST> {
            val safeId = id ?: return Failure(SyntaxError.MISSING_IDENTIFIER)
            val safeValue = value ?: return Failure(SyntaxError.INCOMPLETE_STATEMENT)
            return Success(AST.Assignment(safeId, safeValue))
        }
    }

    data class CallBuilder(
        val functionName: PrintScriptFunctions? = null
    ) : ASTBuilder {
        override fun build(): Either<SyntaxError, AST> {
            val safeName = functionName ?: return Failure(SyntaxError.MISSING_FUNCTION_NAME)
            return Success(AST.Call(safeName))
        }
    }
}