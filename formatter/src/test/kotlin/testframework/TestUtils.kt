package testframework

import ast.AST
import ast.ASTDataType
import ast.ASTIdentifier
import ast.Expression
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue

fun createDeclaration(
    name: String,
    type: PrintScriptType = PrintScriptType.STRING,
    value: Expression? = createStringLiteralExpression("test"),
): AST.Declaration =
    AST.Declaration(
        id = ASTIdentifier(name),
        type = ASTDataType(type),
        value = value,
    )

fun createAssignment(
    name: String,
    value: Expression = createNumberLiteralExpression(1),
): AST.Assignment =
    AST.Assignment(
        id = ASTIdentifier(name),
        value = value,
    )

fun createPrintln(vararg args: Expression): AST.Call =
    AST.Call(
        functionName = PrintScriptFunctions.PRINTLN,
        args = args.toList(),
    )

fun createNumberLiteralExpression(value: Number): Expression.Literal = Expression.Literal(PrintScriptValue.NumberLiteral(value))

fun createStringLiteralExpression(value: String): Expression.Literal = Expression.Literal(PrintScriptValue.StringLiteral(value))

fun createVariableExpression(name: String): Expression.Variable = Expression.Variable(name)

fun createOperationExpression(
    left: Expression,
    right: Expression,
    operator: PrintScriptOperator = PrintScriptOperator.SUM,
): Expression.Operation = Expression.Operation(left, right, operator)
