package interpreter.environment

import ast.Expression.Literal
import domain.PSType

data class VariableInfo(
    val type: PSType,
    val value: Literal?,
    val mutable: Boolean,
)
