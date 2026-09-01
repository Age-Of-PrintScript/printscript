package parser

import ast.AST
import ast.ASTDataType
import ast.ASTIdentifier
import ast.Expression
import domain.Either
import domain.Failure
import domain.PrintScriptFunctions
import domain.Success

enum class BuilderType { NONE, DECLARATION, ASSIGNMENT, CALL }

data class ASTBuilder(
    val type: BuilderType = BuilderType.NONE,
    val id: ASTIdentifier? = null,
    val dataType: ASTDataType? = null,
    val functionName: PrintScriptFunctions? = null,
    val value: Expression? = null,
    val expressions: List<Expression> = emptyList(),
) {
    fun addExpression(expr: Expression): ASTBuilder =
        when (type) {
            BuilderType.ASSIGNMENT -> copy(value = expr)
            BuilderType.DECLARATION -> copy(value = expr)
            BuilderType.CALL -> copy(expressions = expressions + expr)
            BuilderType.NONE -> this
        }

    fun build(): Either<SyntaxError, AST> =
        when (type) {
            BuilderType.NONE -> Failure(SyntaxError.INVALID_TOKEN)
            BuilderType.DECLARATION -> {
                val safeId = id ?: return Failure(SyntaxError.MISSING_IDENTIFIER)
                val safeType = dataType ?: return Failure(SyntaxError.MISSING_TYPE_IN_DECLARATION)
                Success(AST.Declaration(safeId, safeType, value))
            }
            BuilderType.ASSIGNMENT -> {
                val safeId = id ?: return Failure(SyntaxError.MISSING_IDENTIFIER)
                val safeValue = value ?: return Failure(SyntaxError.INCOMPLETE_STATEMENT)
                Success(AST.Assignment(safeId, safeValue))
            }
            BuilderType.CALL -> {
                val safeName = functionName ?: return Failure(SyntaxError.MISSING_FUNCTION_NAME)
                Success(AST.Call(safeName, expressions))
            }
        }
}
