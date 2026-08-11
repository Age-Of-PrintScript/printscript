package parser

import ast.Expression
import kotlin.test.assertEquals
import kotlin.test.Test

typealias Num = PrintScriptValue.NumberLiteral

class ExpressionTest {

    private val solver = ExpressionSolver()


    private fun assertExpressionEquals(expression: Expression, expectedValue: Number) {
        assertEquals(expectedValue.toDouble(), solver.solve(expression))
    }

    @Test
    fun `la multiplicacion tiene precedencia sobre la suma`() {
        // 2 + (3 * 4) = 14, no (2 + 3) * 4 = 20
        val expression = Expression.Operation(
            left = Expression.Literal(Num(2)),
            operator = PrintScriptOperator.SUM,
            right = Expression.Operation(
                Expression.Literal(Num(3)),
                    Expression.Literal(Num(4)),
                    PrintScriptOperator.MULTIPLY)
        )
        assertExpressionEquals(expression, 14)
    }

    @Test
    fun `suma simple`() {
        val expression = Expression.Operation(
            Expression.Literal(Num(2)),
        Expression.Literal(Num(3)),
            PrintScriptOperator.SUM
        )
        assertExpressionEquals(expression, 5)
    }

    @Test
    fun `resta simple`() {
        val expression = Expression.Operation(
            Expression.Literal(Num(10)),
            Expression.Literal(Num(4)),
            PrintScriptOperator.SUBTRACT
        )
        assertExpressionEquals(expression, 6)
    }

    @Test
    fun `division simple`() {
        val expression = Expression.Operation(
            Expression.Literal(Num(20)),
            Expression.Literal(Num(5)),
            PrintScriptOperator.DIVIDE
        )
        assertExpressionEquals(expression, 4)

    }


}




