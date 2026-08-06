
sealed interface Expression

data class Literal(val value: PrintScriptValue): Expression

data class Variable(val name: String): Expression

data class Operation(
    val left: Expression,
    val right: Expression,
    val operator: PrintScriptOperator
): Expression
