package ast

import domain.PSType
import domain.Position

enum class ASTType {
    DECLARATION,
    ASSIGNMENT,
    EXPRESSION_STATEMENT,
}

sealed interface AST {
    val astType: ASTType

    data class Declaration(
        val id: String,
        val type: PSType,
        val mutable: Boolean,
        val value: Expression?,
        override val astType: ASTType = ASTType.DECLARATION,
    ) : AST

    data class Assignment(
        val id: String,
        val value: Expression,
        override val astType: ASTType = ASTType.ASSIGNMENT,
    ) : AST

    data class ExpressionStatement(
        val expression: Expression,
        override val astType: ASTType = ASTType.EXPRESSION_STATEMENT,
    ) : AST
}

data class Program(
    val trees: List<AST>,
    val start: Position,
    val end: Position,
)
