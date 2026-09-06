package linter.rules

import ast.ASTDataType
import ast.ASTIdentifier
import ast.ASTViejo
import ast.ExpressionViejo
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue

fun createDeclaration(
    name: String,
    type: PrintScriptType = PrintScriptType.STRING,
    value: ExpressionViejo? = createLiteralExpression("test"),
): ASTViejo.Declaration =
    ASTViejo.Declaration(
        id = ASTIdentifier(name),
        type = ASTDataType(type),
        value = value,
    )

fun createAssignment(
    name: String,
    value: ExpressionViejo = createLiteralExpression(1),
): ASTViejo.Assignment =
    ASTViejo.Assignment(
        id = ASTIdentifier(name),
        value = value,
    )

fun createPrintln(vararg args: ExpressionViejo): ASTViejo.Call =
    ASTViejo.Call(
        functionName = PrintScriptFunctions.PRINTLN,
        args = args.toList(),
    )

fun createLiteralExpression(value: Number): ExpressionViejo.Literal = ExpressionViejo.Literal(PrintScriptValue.NumberLiteral(value))

fun createLiteralExpression(value: String): ExpressionViejo.Literal = ExpressionViejo.Literal(PrintScriptValue.StringLiteral(value))

fun createVariableExpression(name: String): ExpressionViejo.Variable = ExpressionViejo.Variable(name)

fun createOperationExpression(
    left: ExpressionViejo,
    right: ExpressionViejo,
    operator: PrintScriptOperator = PrintScriptOperator.SUM,
): ExpressionViejo.Operation = ExpressionViejo.Operation(left, right, operator)
