package interpreter.environment

import domain.PSLiteral
import domain.PSType

data class VariableInfo(
    val type: PSType,
    val value: PSLiteral?,
    val mutable: Boolean = true,
)
