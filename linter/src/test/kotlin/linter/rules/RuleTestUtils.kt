package linter.rules

import ast.ASTDataType
import ast.ASTIdentifier
import ast.ASTViejo
import ast.Expression
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue

fun createDeclaration(
    name: String,
    type: PrintScriptType = PrintScriptType.STRING,
    value: Expression? = createLiteralExpression("test"),
): ASTViejo.Declaration =
    ASTViejo.Declaration(
        id = ASTIdentifier(name),
        type = ASTDataType(type),
        value = value,
    )

fun createAssignment(
    name: String,
    value: Expression = createLiteralExpression(1),
): ASTViejo.Assignment =
    ASTViejo.Assignment(
        id = ASTIdentifier(name),
        value = value,
    )

fun createPrintln(vararg args: Expression): ASTViejo.Call =
    ASTViejo.Call(
        functionName = PrintScriptFunctions.PRINTLN,
        args = args.toList(),
    )

fun createLiteralExpression(value: Number): Expression.Literal = Expression.Literal(PrintScriptValue.NumberLiteral(value))

fun createLiteralExpression(value: String): Expression.Literal = Expression.Literal(PrintScriptValue.StringLiteral(value))

fun createVariableExpression(name: String): Expression.Variable = Expression.Variable(name)

fun createOperationExpression(
    left: Expression,
    right: Expression,
    operator: PrintScriptOperator = PrintScriptOperator.SUM,
): Expression.Operation = Expression.Operation(left, right, operator)
