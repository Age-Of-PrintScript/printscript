package ast

import domain.PSOperator
import domain.PSType

sealed interface Expression {
    data class Literal(
        val value: String,
        val type: PSType,
    ) : Expression

    data class Variable(
        val name: String,
    ) : Expression

    data class Operation(
        val left: Expression,
        val operator: PSOperator,
        val right: Expression,
    ) : Expression

    data class Call(
        val name: String,
        val args: List<Expression>,
    ) : Expression
}
