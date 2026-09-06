package ast

import domain.PrintScriptOperator
import domain.PrintScriptValue

sealed interface ExpressionViejo {
    data class Literal(
        val value: PrintScriptValue,
    ) : ExpressionViejo

    data class Variable(
        val name: String,
    ) : ExpressionViejo

    data class Operation(
        val left: ExpressionViejo,
        val right: ExpressionViejo,
        val operator: PrintScriptOperator,
    ) : ExpressionViejo
}
