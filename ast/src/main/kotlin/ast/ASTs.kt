package ast

import domain.PSType

enum class ASTType {
    DECLARATION,
    ASSIGNMENT,
    EXPRESSION_STATEMENT,
}

sealed interface AST {
    data class Declaration(
        val id: String,
        val type: PSType,
        val mutable: Boolean,
        val value: Expression?,
    ) : AST

    data class Assignment(
        val id: String,
        val value: Expression,
    ) : AST

    data class ExpressionStatement(
        val expression: Expression,
    ) : AST
}
