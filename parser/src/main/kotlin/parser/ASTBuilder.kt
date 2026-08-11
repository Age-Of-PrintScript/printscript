package parser

import Either
import Failure
import PrintScriptFunctions
import Success
import ast.AST
import ast.ASTDataType
import ast.ASTIdentifier
import ast.Expression

internal sealed interface ASTBuilder {
    fun build(): Either<SYNTAX_ERROR, AST>
    object EmptyBuilder : ASTBuilder{
        override fun build(): Either<SYNTAX_ERROR, AST> {
            return Failure(SYNTAX_ERROR("Unexpected token"))
        }
    }

    data class DeclarationBuilder(
        val id: ASTIdentifier? = null,
        val type: ASTDataType? = null,
        val value: Expression? = null
    ) : ASTBuilder {
        override fun build(): Either<SYNTAX_ERROR, AST> {
            val safeId = id ?: return Failure(SYNTAX_ERROR("Missing identifier in declaration"))
            val safeType = type ?: return Failure(SYNTAX_ERROR("Missing type in declaration"))
            return Success(AST.Declaration(safeId, safeType, value))
        }
    }

    data class AssignmentBuilder(
        val id: ASTIdentifier? = null,
        val value: Expression? = null
    ) : ASTBuilder {
        override fun build(): Either<SYNTAX_ERROR, AST> {
            val safeId = id ?: return Failure(SYNTAX_ERROR("Missing identifier in assignment"))
            val safeValue = value ?: return Failure(SYNTAX_ERROR("Missing value in assignment"))
            return Success(AST.Assignment(safeId, safeValue))
        }
    }

    data class CallBuilder(
        val functionName: PrintScriptFunctions? = null
    ) : ASTBuilder {
        override fun build(): Either<SYNTAX_ERROR, AST> {
            val safeName = functionName ?: return Failure(SYNTAX_ERROR("Missing function name in call"))
            return Success(AST.Call(safeName))
        }
    }
}