package interpreter.cases

import ast.Expression
import domain.PrintScriptOperator
import domain.PrintScriptValue

val LITERAL_NUMBER_1 = Expression.Literal(PrintScriptValue.NumberLiteral(1))
val LITERAL_NUMBER_2 = Expression.Literal(PrintScriptValue.NumberLiteral(2))
val LITERAL_NUMBER_5 = Expression.Literal(PrintScriptValue.NumberLiteral(5))
val LITERAL_NUMBER_10 = Expression.Literal(PrintScriptValue.NumberLiteral(10))

val LITERAL_STRING_HOLA = Expression.Literal(PrintScriptValue.StringLiteral("hola"))
val LITERAL_STRING_X = Expression.Literal(PrintScriptValue.StringLiteral("x"))
val LITERAL_STRING_MUNDO = Expression.Literal(PrintScriptValue.StringLiteral(" mundo"))

val VARIABLE_X = Expression.Variable("x")
val VARIABLE_A = Expression.Variable("a")
val VARIABLE_Y = Expression.Variable("y")

val OPERATION_1_PLUS_2 =
    Expression.Operation(
        left = LITERAL_NUMBER_1,
        right = LITERAL_NUMBER_2,
        operator = PrintScriptOperator.SUM,
    )

val OPERATION_5_DIVIDE_STRING =
    Expression.Operation(
        left = LITERAL_NUMBER_5,
        right = LITERAL_STRING_X,
        operator = PrintScriptOperator.DIVIDE,
    )

val OPERATION_X_PLUS_1 =
    Expression.Operation(
        left = VARIABLE_X,
        right = LITERAL_NUMBER_1,
        operator = PrintScriptOperator.SUM,
    )

val OPERATION_STRING_PLUS_NUMBER =
    Expression.Operation(
        left = LITERAL_STRING_HOLA,
        right = LITERAL_NUMBER_5,
        operator = PrintScriptOperator.SUM,
    )

val OPERATION_NUMBER_PLUS_STRING =
    Expression.Operation(
        left = LITERAL_NUMBER_5,
        right = LITERAL_STRING_HOLA,
        operator = PrintScriptOperator.SUM,
    )

val OPERATION_STRING_PLUS_STRING =
    Expression.Operation(
        left = LITERAL_STRING_HOLA,
        right = LITERAL_STRING_MUNDO,
        operator = PrintScriptOperator.SUM,
    )
