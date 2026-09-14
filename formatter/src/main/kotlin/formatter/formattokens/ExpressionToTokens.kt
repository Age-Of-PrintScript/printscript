package formatter.formattokens

import ast.Expression
import domain.StrType

fun expressionToFormatTokens(expression: Expression): List<FormatToken> =
    when (expression) {
        is Expression.Literal -> {
            if (expression.type == StrType) {
                listOf(Text("\"")) + listOf(Text(expression.value)) + listOf(Text("\""))
            } else {
                listOf(Text(expression.value))
            }
        }
        is Expression.Variable -> listOf(Text(expression.name))
        is Expression.Operation -> operationToFormatTokens(expression)
        is Expression.Call -> callToFormatTokens(expression)
    }

private fun operationToFormatTokens(operation: Expression.Operation): List<FormatToken> =
    expressionToFormatTokens(operation.left) +
        Text(operation.operator.symbol) +
        expressionToFormatTokens(operation.right)

private fun callToFormatTokens(call: Expression.Call): List<FormatToken> {
    val argsTokens =
        call.args.flatMapIndexed { index, arg ->
            if (index == 0) expressionToFormatTokens(arg) else listOf(Text(","), WhiteSpace) + expressionToFormatTokens(arg)
        }
    return listOf(Text(call.name), Text("(")) + argsTokens + listOf(Text(")"))
}
