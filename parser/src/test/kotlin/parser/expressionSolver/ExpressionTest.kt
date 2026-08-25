package parser.expressionSolver

import ast.Expression
import ast.ExpressionSolver
import domain.Either
import domain.Failure
import domain.PrintScriptOperator
import domain.PrintScriptValue
import domain.Success
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals

typealias Num = PrintScriptValue.NumberLiteral

class ExpressionTest {
    private val solver = ExpressionSolver()

    private fun assertExpressionEquals(
        expression: Expression,
        expectedValue: Number,
        values: Map<String, Optional<PrintScriptValue>> = emptyMap(),
    ): Either<String, PrintScriptValue> {
        when (val result = solver.solve(expression, values)) {
            is Success -> {
                val value = result.value
                if (value !is PrintScriptValue.NumberLiteral) {
                    return Failure("NumberLiteral Expected, got $value instead")
                }
                assertEquals(expectedValue.toDouble(), value.value.toDouble())
            }
            is Failure -> return Failure("Se esperaba un resultado exitoso, pero fallo con: ${result.value}")
        }
        return Failure("something went wrong")
    }

    @Test
    fun `suma simple`() {
        val expression =
            Expression.Operation(
                Expression.Literal(Num(2)),
                Expression.Literal(Num(3)),
                PrintScriptOperator.SUM,
            )
        assertExpressionEquals(expression, 5)
    }

    @Test
    fun `resta simple`() {
        val expression =
            Expression.Operation(
                Expression.Literal(Num(10)),
                Expression.Literal(Num(4)),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expression, 6)
    }

    @Test
    fun `division simple`() {
        val expression =
            Expression.Operation(
                Expression.Literal(Num(20)),
                Expression.Literal(Num(5)),
                PrintScriptOperator.DIVIDE,
            )
        assertExpressionEquals(expression, 4)
    }

    @Test
    fun `la multiplicacion tiene precedencia sobre la suma`() {
        // 2 + (3 * 4) = 14, no (2 + 3) * 4 = 20
        val expression =
            Expression.Operation(
                left = Expression.Literal(Num(2)),
                operator = PrintScriptOperator.SUM,
                right =
                Expression.Operation(
                    Expression.Literal(Num(3)),
                    Expression.Literal(Num(4)),
                    PrintScriptOperator.MULTIPLY,
                ),
            )
        assertExpressionEquals(expression, 14)
    }

    @Test
    fun `la division tiene precedencia sobre la suma`() {
        // 2 + (8 / 4) = 4, no (2 + 8) / 4 = 2.5
        val expression =
            Expression.Operation(
                Expression.Literal(Num(2)),
                Expression.Operation(
                    Expression.Literal(Num(8)),
                    Expression.Literal(Num(4)),
                    PrintScriptOperator.DIVIDE,
                ),
                PrintScriptOperator.SUM,
            )
        assertExpressionEquals(expression, 4)
    }

    @Test
    fun `la multiplicacion tiene precedencia sobre la resta`() {
        // 10 - (3 * 4) = -2, no (10 - 3) * 4 = 28
        val expression =
            Expression.Operation(
                Expression.Literal(Num(10)),
                Expression.Operation(
                    Expression.Literal(Num(3)),
                    Expression.Literal(Num(4)),
                    PrintScriptOperator.MULTIPLY,
                ),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expression, -2)
    }

    @Test
    fun `la division tiene precedencia sobre la resta`() {
        // 10 - (8 / 4) = 8, no (10 - 8) / 4 = 0.5
        val expression =
            Expression.Operation(
                Expression.Literal(Num(10)),
                Expression.Operation(
                    Expression.Literal(Num(8)),
                    Expression.Literal(Num(4)),
                    PrintScriptOperator.DIVIDE,
                ),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expression, 8)
    }

    // ---------- Asociatividad: misma precedencia, evaluación de izquierda a derecha ----------

    @Test
    fun `la resta es asociativa a izquierda`() {
        // (2 - 3) - 4 = -5, no 2 - (3 - 4) = 3
        val expression =
            Expression.Operation(
                Expression.Operation(
                    Expression.Literal(Num(2)),
                    Expression.Literal(Num(3)),
                    PrintScriptOperator.SUBTRACT,
                ),
                Expression.Literal(Num(4)),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expression, -5)
    }

    @Test
    fun `la division es asociativa a izquierda`() {
        // (20 / 4) / 2 = 2.5, no 20 / (4 / 2) = 10
        val expression =
            Expression.Operation(
                Expression.Operation(
                    Expression.Literal(Num(20)),
                    Expression.Literal(Num(4)),
                    PrintScriptOperator.DIVIDE,
                ),
                Expression.Literal(Num(2)),
                PrintScriptOperator.DIVIDE,
            )
        assertExpressionEquals(expression, 2.5)
    }

    @Test
    fun `multiplicacion y division tienen la misma precedencia y se asocian a izquierda`() {
        // (20 / 4) * 2 = 10, no 20 / (4 * 2) = 2.5
        val expression =
            Expression.Operation(
                Expression.Operation(
                    Expression.Literal(Num(20)),
                    Expression.Literal(Num(4)),
                    PrintScriptOperator.DIVIDE,
                ),
                Expression.Literal(Num(2)),
                PrintScriptOperator.MULTIPLY,
            )
        assertExpressionEquals(expression, 10)
    }

    // ---------- Combinaciones con varios operadores ----------

    @Test
    fun `combinacion de suma resta y multiplicacion respeta precedencia`() {
        // 2 + (3 * 4) - 5 = 9
        val expression =
            Expression.Operation(
                Expression.Operation(
                    Expression.Literal(Num(2)),
                    Expression.Operation(
                        Expression.Literal(Num(3)),
                        Expression.Literal(Num(4)),
                        PrintScriptOperator.MULTIPLY,
                    ),
                    PrintScriptOperator.SUM,
                ),
                Expression.Literal(Num(5)),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expression, 9)
    }

    @Test
    fun `combinacion de division y suma respeta precedencia`() {
        // (8 / 2) + (3 * 2) = 10
        val expression =
            Expression.Operation(
                Expression.Operation(
                    Expression.Literal(Num(8)),
                    Expression.Literal(Num(2)),
                    PrintScriptOperator.DIVIDE,
                ),
                Expression.Operation(
                    Expression.Literal(Num(3)),
                    Expression.Literal(Num(2)),
                    PrintScriptOperator.MULTIPLY,
                ),
                PrintScriptOperator.SUM,
            )
        assertExpressionEquals(expression, 10)
    }

    // ---------- Paréntesis explícitos (agrupación forzada en el AST) ----------

    @Test
    fun `los parentesis fuerzan la suma antes que la multiplicacion`() {
        // (2 + 3) * 4 = 20
        val expression =
            Expression.Operation(
                Expression.Operation(
                    Expression.Literal(Num(2)),
                    Expression.Literal(Num(3)),
                    PrintScriptOperator.SUM,
                ),
                Expression.Literal(Num(4)),
                PrintScriptOperator.MULTIPLY,
            )
        assertExpressionEquals(expression, 20)
    }

    @Test
    fun `los parentesis fuerzan la resta antes que la division`() {
        // (10 - 2) / 4 = 2
        val expression =
            Expression.Operation(
                Expression.Operation(
                    Expression.Literal(Num(10)),
                    Expression.Literal(Num(2)),
                    PrintScriptOperator.SUBTRACT,
                ),
                Expression.Literal(Num(4)),
                PrintScriptOperator.DIVIDE,
            )
        assertExpressionEquals(expression, 2)
    }
}
