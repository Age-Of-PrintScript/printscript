package linter.rules

import ast.AST
import ast.Expression
import domain.NumType
import domain.PSOperator
import domain.PSType
import domain.StrType
import versionfactory.v1_0.Operators

fun createDeclaration(
    name: String,
    type: PSType = StrType,
    value: Expression? = createLiteralExpression("test"),
): AST.DeclarationStatement =
    AST.DeclarationStatement(
        id = name,
        type = type,
        mutable = true,
        value = value,
    )

fun createAssignment(
    name: String,
    value: Expression = createLiteralExpression(1),
): AST.AssignmentStatement =
    AST.AssignmentStatement(
        id = name,
        value = value,
    )

fun createPrintln(vararg args: Expression): AST.ExpressionStatement =
    AST.ExpressionStatement(
        expression =
            Expression.Call(
                name = "println",
                args = args.toList(),
            ),
    )

fun createLiteralExpression(value: Number): Expression.Literal = Expression.Literal(value.toString(), NumType)

fun createLiteralExpression(value: String): Expression.Literal = Expression.Literal(value, StrType)

fun createVariableExpression(name: String): Expression.Variable = Expression.Variable(name)

fun createOperationExpression(
    left: Expression,
    right: Expression,
    operator: PSOperator = Operators.SUM,
): Expression.Operation = Expression.Operation(left, operator, right)
