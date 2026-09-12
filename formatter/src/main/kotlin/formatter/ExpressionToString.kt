package formatter

import ast.Expression

fun expressionToString(expression: Expression): String =
    when (expression) {
        is Expression.Call -> "${expression.name}(${expression.args})"
        is Expression.Literal -> expression.value
        is Expression.Variable -> expression.name
        is Expression.Operation -> operationToString(expression)
    }

fun operationToString(operation: Expression.Operation): String = expressionToString(operation.right) + operation.operator + expressionToString(operation.left)
