package formattest

import formatter.FormatterImplementation
import formatter.FormattingRules
import formatter.LineBeforeCallRule
import formatter.SemiColonAtTheEndRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import testframework.createNumberLiteralExpression
import testframework.createOperationExpression
import testframework.createPrintln
import testframework.createStringLiteralExpression
import testframework.createVariableExpression

class CallTests {
    // ---- Grupo A: combinaciones de reglas activas/inactivas ----

    @Test
    fun `sin lineas antes y con punto y coma`() {
        val call =
            createPrintln(
                createNumberLiteralExpression(5),
            )
        val rules = listOf(LineBeforeCallRule(0), SemiColonAtTheEndRule(true))
        val result = FormatterImplementation(FormattingRules(rules)).format(call)

        assertEquals("println(5);", result)
    }

    @Test
    fun `con dos lineas antes y con punto y coma`() {
        val call =
            createPrintln(
                createNumberLiteralExpression(5),
            )
        val rules = listOf(LineBeforeCallRule(2), SemiColonAtTheEndRule(true))
        val result = FormatterImplementation(FormattingRules(rules)).format(call)

        assertEquals("\n\nprintln(5);", result)
    }

    @Test
    fun `con una linea antes y sin punto y coma`() {
        val call =
            createPrintln(
                createNumberLiteralExpression(5),
            )
        val rules =
            listOf(
                LineBeforeCallRule(1),
                SemiColonAtTheEndRule(false),
            )
        val result =
            FormatterImplementation(
                FormattingRules(rules),
            ).format(call)

        assertEquals("\nprintln(5)", result)
    }

    // ---- Grupo B: contenido del argumento (0 lineas, con punto y coma) ----

    private val allCallRules = listOf(LineBeforeCallRule(0), SemiColonAtTheEndRule(true))

    @Test
    fun `println con un string`() {
        val call =
            createPrintln(
                createStringLiteralExpression("hello"),
            )
        val result =
            FormatterImplementation(
                FormattingRules(allCallRules),
            ).format(call)

        assertEquals("println(\"hello\");", result)
    }

    @Test
    fun `println con una variable`() {
        val call =
            createPrintln(
                createVariableExpression("x"),
            )
        val result =
            FormatterImplementation(
                FormattingRules(allCallRules),
            ).format(call)

        assertEquals("println(x);", result)
    }

    @Test
    fun `println con una operacion`() {
        val call =
            createPrintln(
                createOperationExpression(
                    createNumberLiteralExpression(2),
                    createNumberLiteralExpression(3),
                ),
            )
        val result = FormatterImplementation(FormattingRules(allCallRules)).format(call)

        assertEquals("println(2 + 3);", result)
    }

    // ---- Grupo C: casos limite que documentan bugs reales de callToString ----

    @Test
    fun `println con multiples argumentos solo formatea el primero (bug conocido)`() {
        val call =
            createPrintln(
                createNumberLiteralExpression(1),
                createNumberLiteralExpression(2),
                createNumberLiteralExpression(3),
            )
        val result = FormatterImplementation(FormattingRules(allCallRules)).format(call)

        // callToString solo usa args[0]; el 2 y el 3 se pierden en silencio.
        assertEquals("println(1);", result)
    }

    @Test
    fun `println sin argumentos explota (bug conocido)`() {
        val call = createPrintln()

        assertThrows(IndexOutOfBoundsException::class.java) {
            FormatterImplementation(
                FormattingRules(allCallRules),
            ).format(call)
        }
    }

    @Test
    fun `LineBeforeCallRule con lineas negativas explota (bug conocido)`() {
        val call = createPrintln(createNumberLiteralExpression(5))
        val rules = listOf(LineBeforeCallRule(-1), SemiColonAtTheEndRule(true))

        assertThrows(IllegalArgumentException::class.java) {
            FormatterImplementation(FormattingRules(rules)).format(call)
        }
    }
}
