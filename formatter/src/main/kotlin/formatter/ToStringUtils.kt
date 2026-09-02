package formatter

import ast.AST
import ast.Expression
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue

fun astToString(ast: AST): String =
    when (ast) {
        is AST.Assignment -> assignmentToString(ast)
        is AST.Call -> callToString(ast)
        is AST.Declaration -> declarationToString(ast)
    }

fun declarationToString(declaration: AST.Declaration): String {
    var assignPart = ""
    if (declaration.value != null) {
        val string = expressionToString(declaration.value, 0)
        assignPart = "=$string"
    }
    return "let ${declaration.id.name}:${typeToString(declaration.type.name)}$assignPart"
}

fun assignmentToString(assignment: AST.Assignment): String = "${assignment.id.name}=${expressionToString(assignment.value, 0)}"

fun callToString(call: AST.Call): String = "${functionNameToString(call.functionName)}(${expressionToString(call.args[0], 0)})"

fun expressionToString(
    expression: Expression?,
    parentPrecedence: Int,
): String {
    if (expression == null) return ""

    return when (expression) {
        is Expression.Literal -> literalToString(expression.value)
        is Expression.Operation -> operationToString(expression, parentPrecedence)
        is Expression.Variable -> expression.name
    }
}

fun operationToString(
    operation: Expression.Operation,
    parentPrecedence: Int,
): String {
    val precedence = precedenceOf(operation.operator)

    val left = expressionToString(operation.left, precedence)
    val right = expressionToString(operation.right, precedence + 1) // +1 fuerza paréntesis en empates, por la asociatividad a izquierda

    val result = "$left ${operatorToString(operation.operator)} $right"

    return if (precedence < parentPrecedence) "($result)" else result // si la operación de ahora es una suma o resta, ponele parentesis
}

fun operatorToString(operator: PrintScriptOperator): String =
    when (operator) {
        PrintScriptOperator.SUM -> "+"
        PrintScriptOperator.SUBTRACT -> "-"
        PrintScriptOperator.MULTIPLY -> "*"
        PrintScriptOperator.DIVIDE -> "/"
    }

fun typeToString(type: PrintScriptType): String =
    when (type) {
        PrintScriptType.NUMBER -> "Number"
        PrintScriptType.STRING -> "String"
    }

fun functionNameToString(function: PrintScriptFunctions): String =
    when (function) {
        PrintScriptFunctions.PRINTLN -> "println"
    }

fun literalToString(value: PrintScriptValue): String =
    when (value) {
        is PrintScriptValue.NumberLiteral -> value.value.toString()
        is PrintScriptValue.StringLiteral -> "\"${value.value}\""
    }

private fun precedenceOf(operator: PrintScriptOperator): Int =
    when (operator) {
        PrintScriptOperator.SUM, PrintScriptOperator.SUBTRACT -> 1
        PrintScriptOperator.MULTIPLY, PrintScriptOperator.DIVIDE -> 2
    }
