package interpreter.cases

import ast.ExpressionViejo
import domain.PrintScriptOperator
import domain.PrintScriptValue

val LITERAL_NUMBER_1 = ExpressionViejo.Literal(PrintScriptValue.NumberLiteral(1))
val LITERAL_NUMBER_2 = ExpressionViejo.Literal(PrintScriptValue.NumberLiteral(2))
val LITERAL_NUMBER_5 = ExpressionViejo.Literal(PrintScriptValue.NumberLiteral(5))
val LITERAL_NUMBER_10 = ExpressionViejo.Literal(PrintScriptValue.NumberLiteral(10))

val LITERAL_STRING_HOLA = ExpressionViejo.Literal(PrintScriptValue.StringLiteral("hola"))
val LITERAL_STRING_X = ExpressionViejo.Literal(PrintScriptValue.StringLiteral("x"))
val LITERAL_STRING_MUNDO = ExpressionViejo.Literal(PrintScriptValue.StringLiteral(" mundo"))

val VARIABLE_X = ExpressionViejo.Variable("x")
val VARIABLE_A = ExpressionViejo.Variable("a")
val VARIABLE_Y = ExpressionViejo.Variable("y")

val OPERATION_1_PLUS_2 =
    ExpressionViejo.Operation(
        left = LITERAL_NUMBER_1,
        right = LITERAL_NUMBER_2,
        operator = PrintScriptOperator.SUM,
    )

val OPERATION_5_DIVIDE_STRING =
    ExpressionViejo.Operation(
        left = LITERAL_NUMBER_5,
        right = LITERAL_STRING_X,
        operator = PrintScriptOperator.DIVIDE,
    )

val OPERATION_X_PLUS_1 =
    ExpressionViejo.Operation(
        left = VARIABLE_X,
        right = LITERAL_NUMBER_1,
        operator = PrintScriptOperator.SUM,
    )

val OPERATION_STRING_PLUS_NUMBER =
    ExpressionViejo.Operation(
        left = LITERAL_STRING_HOLA,
        right = LITERAL_NUMBER_5,
        operator = PrintScriptOperator.SUM,
    )

val OPERATION_NUMBER_PLUS_STRING =
    ExpressionViejo.Operation(
        left = LITERAL_NUMBER_5,
        right = LITERAL_STRING_HOLA,
        operator = PrintScriptOperator.SUM,
    )

val OPERATION_STRING_PLUS_STRING =
    ExpressionViejo.Operation(
        left = LITERAL_STRING_HOLA,
        right = LITERAL_STRING_MUNDO,
        operator = PrintScriptOperator.SUM,
    )
