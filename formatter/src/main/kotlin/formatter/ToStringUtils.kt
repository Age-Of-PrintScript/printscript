package formatter

import ast.AST
import ast.Expression
import domain.PSType
import domain.StrType

internal fun astToString(ast: AST): String =
    when (ast) {
        is AST.Assignment -> assignmentToString(ast)
        is AST.ExpressionStatement -> expressionToString(ast.expression, 0)
        is AST.Declaration -> declarationToString(ast)
    }

internal fun declarationToString(declaration: AST.Declaration): String {
    val keyword = if (declaration.mutable) "let" else "const"
    var assignPart = ""
    if (declaration.value != null) {
        val string = expressionToString(declaration.value, 0)
        assignPart = "=$string"
    }
    return "$keyword ${declaration.id}:${typeToString(declaration.type)}$assignPart"
}

internal fun assignmentToString(assignment: AST.Assignment): String = "${assignment.id}=${expressionToString(assignment.value, 0)}"

internal fun callToString(call: Expression.Call): String {
    val argsStr = call.args.joinToString(", ") { expressionToString(it, 0) }
    return "${call.name}($argsStr)"
}

internal fun expressionToString(
    expression: Expression?,
    parentPrecedence: Int = 0,
): String {
    if (expression == null) return ""

    return when (expression) {
        is Expression.Literal -> literalToString(expression)
        is Expression.Operation -> operationToString(expression, parentPrecedence)
        is Expression.Variable -> expression.name
        is Expression.Call -> callToString(expression)
    }
}

internal fun operationToString(
    operation: Expression.Operation,
    parentPrecedence: Int,
): String {
    val precedence = operation.operator.precedence

    val left = expressionToString(operation.left, precedence)
    val right = expressionToString(operation.right, precedence + 1) // +1 fuerza paréntesis en empates, por la asociatividad a izquierda

    val result = "$left ${operation.operator.symbol} $right"

    return if (precedence < parentPrecedence) "($result)" else result // si la operación de ahora es una suma o resta, ponele parentesis
}

internal fun typeToString(type: PSType): String = type.name

internal fun literalToString(literal: Expression.Literal): String =
    if (literal.type == StrType || literal.type.name == "string") {
        "\"${literal.value}\""
    } else {
        literal.value
    }
