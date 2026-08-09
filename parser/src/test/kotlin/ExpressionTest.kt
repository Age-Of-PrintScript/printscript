import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class ExpressionTest {

    private val solver = ExpressionSolver()

    @Test
    fun `la multiplicacion tiene precedencia sobre la suma`() {
        // 2 + (3 * 4) = 14, no (2 + 3) * 4 = 20
        val expression = Operation(
            left = Num(2),
            operator = PrintScriptOperator.SUM,
            right = Operation(Num(3), Num(4), PrintScriptOperator.MULTIPLY)
        )

        assertEquals(14, solver.solve(expression))
    }

    @Test
    fun `suma simple`() {
        val expression = Operation(Num(2), Num(3), PrintScriptOperator.SUM)
        assertEquals(5, solver.solve(expression))
    }

    @Test
    fun `resta simple`() {
        val expression = Operation(Num(10), Num(4), PrintScriptOperator.SUBTRACT)
        assertEquals(6, solver.solve(expression))
    }

    @Test
    fun `division simple`() {
        val expression = Operation(Num(20), Num(5), PrintScriptOperator.DIVIDE)
        assertEquals(4, solver.solve(expression))
    }


}




