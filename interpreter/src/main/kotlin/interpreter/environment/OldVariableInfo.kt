package interpreter.environment

import ast.Expression.Literal
import domain.PSType
import domain.PrintScriptType
import domain.PrintScriptValue
import java.util.Optional

data class OldVariableInfo(
    val type: PrintScriptType,
    val value: Optional<PrintScriptValue>,
)

data class VariableInfo(
    val type: PSType,
    val value: Literal?,
)
