package formatter

import ast.Expression
import domain.PrintScriptOperator
import domain.PrintScriptValue

fun expressionToString(expression: Expression?, parentPrecedence: Int): String {
    if (expression == null) return ""

    return when (expression) {
        is Expression.Literal -> literalToString(expression.value)
        is Expression.Operation -> operationToString(expression, parentPrecedence)
        is Expression.Variable -> expression.name
    }
}

fun operationToString(operation: Expression.Operation, parentPrecedence: Int): String{

    val precedence = precedenceOf(operation.operator)

    val left = expressionToString(operation.left, precedence)
    val right = expressionToString(operation.right, precedence + 1) // +1 fuerza paréntesis en empates, por la asociatividad a izquierda

    val result = "$left ${operatorToString(operation.operator)} $right"

    return if (precedence < parentPrecedence) "($result)" else result

}



fun operatorToString(operator: PrintScriptOperator): String{
    return when (operator){
        PrintScriptOperator.SUM -> "+"
        PrintScriptOperator.SUBTRACT -> "-"
        PrintScriptOperator.MULTIPLY -> "*"
        PrintScriptOperator.DIVIDE -> "/"
    }

}

fun literalToString(value: PrintScriptValue): String{
    return when (value) {
        is PrintScriptValue.NumberLiteral -> value.value.toString()
        is PrintScriptValue.StringLiteral -> "\"${value.value}\""
    }
}

private fun precedenceOf(operator: PrintScriptOperator): Int {
    return when (operator) {
        PrintScriptOperator.SUM, PrintScriptOperator.SUBTRACT -> 1
        PrintScriptOperator.MULTIPLY, PrintScriptOperator.DIVIDE -> 2
    }
}