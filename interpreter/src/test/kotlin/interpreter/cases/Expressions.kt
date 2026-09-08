package interpreter.cases

import ast.Expression
import domain.NumType
import domain.StrType
import interpreter.Operators

val LITERAL_NUMBER_1 = Expression.Literal("1", NumType)
val LITERAL_NUMBER_2 = Expression.Literal("2", NumType)
val LITERAL_NUMBER_5 = Expression.Literal("5", NumType)
val LITERAL_NUMBER_10 = Expression.Literal("10", NumType)

val LITERAL_STRING_HOLA = Expression.Literal("hola", StrType)
val LITERAL_STRING_X = Expression.Literal("x", StrType)
val LITERAL_STRING_MUNDO = Expression.Literal(" mundo", StrType)

val VARIABLE_X = Expression.Variable("x")
val VARIABLE_A = Expression.Variable("a")
val VARIABLE_Y = Expression.Variable("y")

val OPERATION_1_PLUS_2 =
    Expression.Operation(
        left = LITERAL_NUMBER_1,
        operator = Operators.SUM,
        right = LITERAL_NUMBER_2,
    )

val OPERATION_5_DIVIDE_STRING =
    Expression.Operation(
        left = LITERAL_NUMBER_5,
        operator = Operators.DIVIDE,
        right = LITERAL_STRING_X,
    )

val OPERATION_X_PLUS_1 =
    Expression.Operation(
        left = VARIABLE_X,
        operator = Operators.SUM,
        right = LITERAL_NUMBER_1,
    )

val OPERATION_STRING_PLUS_NUMBER =
    Expression.Operation(
        left = LITERAL_STRING_HOLA,
        operator = Operators.SUM,
        right = LITERAL_NUMBER_5,
    )

val OPERATION_NUMBER_PLUS_STRING =
    Expression.Operation(
        left = LITERAL_NUMBER_5,
        operator = Operators.SUM,
        right = LITERAL_STRING_HOLA,
    )

val OPERATION_STRING_PLUS_STRING =
    Expression.Operation(
        left = LITERAL_STRING_HOLA,
        operator = Operators.SUM,
        right = LITERAL_STRING_MUNDO,
    )
