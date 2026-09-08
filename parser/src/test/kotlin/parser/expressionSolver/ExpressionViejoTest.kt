package parser.expressionSolver

import ast.ExpressionViejo
import ast.OldExpressionSolver
import domain.Either
import domain.Failure
import domain.PrintScriptOperator
import domain.PrintScriptValue
import domain.Success
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals

typealias Num = PrintScriptValue.NumberLiteral

class ExpressionViejoTest {
    private val solver = OldExpressionSolver()

    private fun assertExpressionEquals(
        expressionViejo: ExpressionViejo,
        expectedValue: Number,
        values: Map<String, Optional<PrintScriptValue>> = emptyMap(),
    ): Either<String, PrintScriptValue> {
        when (val result = solver.solve(expressionViejo, values)) {
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
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Literal(Num(2)),
                ExpressionViejo.Literal(Num(3)),
                PrintScriptOperator.SUM,
            )
        assertExpressionEquals(expressionViejo, 5)
    }

    @Test
    fun `resta simple`() {
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Literal(Num(10)),
                ExpressionViejo.Literal(Num(4)),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expressionViejo, 6)
    }

    @Test
    fun `division simple`() {
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Literal(Num(20)),
                ExpressionViejo.Literal(Num(5)),
                PrintScriptOperator.DIVIDE,
            )
        assertExpressionEquals(expressionViejo, 4)
    }

    @Test
    fun `la multiplicacion tiene precedencia sobre la suma`() {
        // 2 + (3 * 4) = 14, no (2 + 3) * 4 = 20
        val expressionViejo =
            ExpressionViejo.Operation(
                left = ExpressionViejo.Literal(Num(2)),
                operator = PrintScriptOperator.SUM,
                right =
                    ExpressionViejo.Operation(
                        ExpressionViejo.Literal(Num(3)),
                        ExpressionViejo.Literal(Num(4)),
                        PrintScriptOperator.MULTIPLY,
                    ),
            )
        assertExpressionEquals(expressionViejo, 14)
    }

    @Test
    fun `la division tiene precedencia sobre la suma`() {
        // 2 + (8 / 4) = 4, no (2 + 8) / 4 = 2.5
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Literal(Num(2)),
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(8)),
                    ExpressionViejo.Literal(Num(4)),
                    PrintScriptOperator.DIVIDE,
                ),
                PrintScriptOperator.SUM,
            )
        assertExpressionEquals(expressionViejo, 4)
    }

    @Test
    fun `la multiplicacion tiene precedencia sobre la resta`() {
        // 10 - (3 * 4) = -2, no (10 - 3) * 4 = 28
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Literal(Num(10)),
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(3)),
                    ExpressionViejo.Literal(Num(4)),
                    PrintScriptOperator.MULTIPLY,
                ),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expressionViejo, -2)
    }

    @Test
    fun `la division tiene precedencia sobre la resta`() {
        // 10 - (8 / 4) = 8, no (10 - 8) / 4 = 0.5
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Literal(Num(10)),
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(8)),
                    ExpressionViejo.Literal(Num(4)),
                    PrintScriptOperator.DIVIDE,
                ),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expressionViejo, 8)
    }

    // ---------- Asociatividad: misma precedencia, evaluación de izquierda a derecha ----------

    @Test
    fun `la resta es asociativa a izquierda`() {
        // (2 - 3) - 4 = -5, no 2 - (3 - 4) = 3
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(2)),
                    ExpressionViejo.Literal(Num(3)),
                    PrintScriptOperator.SUBTRACT,
                ),
                ExpressionViejo.Literal(Num(4)),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expressionViejo, -5)
    }

    @Test
    fun `la division es asociativa a izquierda`() {
        // (20 / 4) / 2 = 2.5, no 20 / (4 / 2) = 10
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(20)),
                    ExpressionViejo.Literal(Num(4)),
                    PrintScriptOperator.DIVIDE,
                ),
                ExpressionViejo.Literal(Num(2)),
                PrintScriptOperator.DIVIDE,
            )
        assertExpressionEquals(expressionViejo, 2.5)
    }

    @Test
    fun `multiplicacion y division tienen la misma precedencia y se asocian a izquierda`() {
        // (20 / 4) * 2 = 10, no 20 / (4 * 2) = 2.5
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(20)),
                    ExpressionViejo.Literal(Num(4)),
                    PrintScriptOperator.DIVIDE,
                ),
                ExpressionViejo.Literal(Num(2)),
                PrintScriptOperator.MULTIPLY,
            )
        assertExpressionEquals(expressionViejo, 10)
    }

    // ---------- Combinaciones con varios operadores ----------

    @Test
    fun `combinacion de suma resta y multiplicacion respeta precedencia`() {
        // 2 + (3 * 4) - 5 = 9
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(2)),
                    ExpressionViejo.Operation(
                        ExpressionViejo.Literal(Num(3)),
                        ExpressionViejo.Literal(Num(4)),
                        PrintScriptOperator.MULTIPLY,
                    ),
                    PrintScriptOperator.SUM,
                ),
                ExpressionViejo.Literal(Num(5)),
                PrintScriptOperator.SUBTRACT,
            )
        assertExpressionEquals(expressionViejo, 9)
    }

    @Test
    fun `combinacion de division y suma respeta precedencia`() {
        // (8 / 2) + (3 * 2) = 10
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(8)),
                    ExpressionViejo.Literal(Num(2)),
                    PrintScriptOperator.DIVIDE,
                ),
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(3)),
                    ExpressionViejo.Literal(Num(2)),
                    PrintScriptOperator.MULTIPLY,
                ),
                PrintScriptOperator.SUM,
            )
        assertExpressionEquals(expressionViejo, 10)
    }

    // ---------- Paréntesis explícitos (agrupación forzada en el AST) ----------

    @Test
    fun `los parentesis fuerzan la suma antes que la multiplicacion`() {
        // (2 + 3) * 4 = 20
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(2)),
                    ExpressionViejo.Literal(Num(3)),
                    PrintScriptOperator.SUM,
                ),
                ExpressionViejo.Literal(Num(4)),
                PrintScriptOperator.MULTIPLY,
            )
        assertExpressionEquals(expressionViejo, 20)
    }

    @Test
    fun `los parentesis fuerzan la resta antes que la division`() {
        // (10 - 2) / 4 = 2
        val expressionViejo =
            ExpressionViejo.Operation(
                ExpressionViejo.Operation(
                    ExpressionViejo.Literal(Num(10)),
                    ExpressionViejo.Literal(Num(2)),
                    PrintScriptOperator.SUBTRACT,
                ),
                ExpressionViejo.Literal(Num(4)),
                PrintScriptOperator.DIVIDE,
            )
        assertExpressionEquals(expressionViejo, 2)
    }
}
