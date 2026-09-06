package formatter

import ast.ASTViejo
import ast.ExpressionViejo
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue

internal fun astToString(astViejo: ASTViejo): String =
    when (astViejo) {
        is ASTViejo.Assignment -> assignmentToString(astViejo)
        is ASTViejo.Call -> callToString(astViejo)
        is ASTViejo.Declaration -> declarationToString(astViejo)
    }

internal fun declarationToString(declaration: ASTViejo.Declaration): String {
    var assignPart = ""
    if (declaration.value != null) {
        val string = expressionToString(declaration.value, 0)
        assignPart = "=$string"
    }
    return "let ${declaration.id.name}:${typeToString(declaration.type.name)}$assignPart"
}

internal fun assignmentToString(assignment: ASTViejo.Assignment): String = "${assignment.id.name}=${expressionToString(assignment.value, 0)}"

internal fun callToString(call: ASTViejo.Call): String {
    val argsStr = call.args.joinToString(", ") { expressionToString(it, 0) }
    return "${functionNameToString(call.functionName)}($argsStr)"
}

internal fun expressionToString(
    expressionViejo: ExpressionViejo?,
    parentPrecedence: Int,
): String {
    if (expressionViejo == null) return ""

    return when (expressionViejo) {
        is ExpressionViejo.Literal -> literalToString(expressionViejo.value)
        is ExpressionViejo.Operation -> operationToString(expressionViejo, parentPrecedence)
        is ExpressionViejo.Variable -> expressionViejo.name
    }
}

internal fun operationToString(
    operation: ExpressionViejo.Operation,
    parentPrecedence: Int,
): String {
    val precedence = precedenceOf(operation.operator)

    val left = expressionToString(operation.left, precedence)
    val right = expressionToString(operation.right, precedence + 1) // +1 fuerza paréntesis en empates, por la asociatividad a izquierda

    val result = "$left ${operatorToString(operation.operator)} $right"

    return if (precedence < parentPrecedence) "($result)" else result // si la operación de ahora es una suma o resta, ponele parentesis
}

internal fun operatorToString(operator: PrintScriptOperator): String =
    when (operator) {
        PrintScriptOperator.SUM -> "+"
        PrintScriptOperator.SUBTRACT -> "-"
        PrintScriptOperator.MULTIPLY -> "*"
        PrintScriptOperator.DIVIDE -> "/"
    }

internal fun typeToString(type: PrintScriptType): String =
    when (type) {
        PrintScriptType.NUMBER -> "Number"
        PrintScriptType.STRING -> "String"
    }

internal fun functionNameToString(function: PrintScriptFunctions): String =
    when (function) {
        PrintScriptFunctions.PRINTLN -> "println"
    }

internal fun literalToString(value: PrintScriptValue): String =
    when (value) {
        is PrintScriptValue.NumberLiteral -> value.value.toString()
        is PrintScriptValue.StringLiteral -> "\"${value.value}\""
    }

private fun precedenceOf(operator: PrintScriptOperator): Int =
    when (operator) {
        PrintScriptOperator.SUM, PrintScriptOperator.SUBTRACT -> 1
        PrintScriptOperator.MULTIPLY, PrintScriptOperator.DIVIDE -> 2
    }
