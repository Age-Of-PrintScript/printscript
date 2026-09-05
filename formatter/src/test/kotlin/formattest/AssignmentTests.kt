package formattest

import domain.PrintScriptOperator
import formatter.FormatterImplementation
import formatter.FormattingRules
import formatter.SemiColonAtTheEndRule
import formatter.SpacesAroundAssignRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import testframework.createAssignment
import testframework.createNumberLiteralExpression
import testframework.createOperationExpression
import testframework.createStringLiteralExpression
import testframework.createVariableExpression

// ------------------ Assignment ------------------------------

class AssignmentTests {
    // ---- Grupo A: combinaciones de reglas activas/inactivas ----

    @Test
    fun `todas las reglas activas`() {
        val assignment = createAssignment("x", createNumberLiteralExpression(5))
        val rules = listOf(SpacesAroundAssignRule(true), SemiColonAtTheEndRule(true))
        val result = FormatterImplementation(FormattingRules(rules)).format(assignment)

        assertEquals("x = 5;", result)
    }

    @Test
    fun `todas las reglas inactivas`() {
        val assignment = createAssignment("x", createNumberLiteralExpression(5))
        val rules = listOf(SpacesAroundAssignRule(false), SemiColonAtTheEndRule(false))
        val result = FormatterImplementation(FormattingRules(rules)).format(assignment)

        assertEquals("x=5", result)
    }

    @Test
    fun `solo espacios alrededor del igual activo`() {
        val assignment = createAssignment("x", createNumberLiteralExpression(5))
        val rules = listOf(SpacesAroundAssignRule(true), SemiColonAtTheEndRule(false))
        val result = FormatterImplementation(FormattingRules(rules)).format(assignment)

        assertEquals("x = 5", result)
    }

    @Test
    fun `solo punto y coma activo`() {
        val assignment = createAssignment("x", createNumberLiteralExpression(5))
        val rules = listOf(SpacesAroundAssignRule(false), SemiColonAtTheEndRule(true))
        val result = FormatterImplementation(FormattingRules(rules)).format(assignment)

        assertEquals("x=5;", result)
    }

    // ---- Grupo B: contenido del value (todas las reglas activas) ----

    private val allAssignmentRules = listOf(SpacesAroundAssignRule(true), SemiColonAtTheEndRule(true))

    @Test
    fun `asigna un string`() {
        val assignment = createAssignment("x", createStringLiteralExpression("hello"))
        val result = FormatterImplementation(FormattingRules(allAssignmentRules)).format(assignment)

        assertEquals("x = \"hello\";", result)
    }

    @Test
    fun `asigna un string con signo igual dentro`() {
        val assignment = createAssignment("x", createStringLiteralExpression("a=b"))
        val result = FormatterImplementation(FormattingRules(allAssignmentRules)).format(assignment)

        assertEquals("x = \"a=b\";", result)
    }

    @Test
    fun `asigna un numero negativo`() {
        val assignment = createAssignment("x", createNumberLiteralExpression(-5))
        val result = FormatterImplementation(FormattingRules(allAssignmentRules)).format(assignment)

        assertEquals("x = -5;", result)
    }

    @Test
    fun `asigna el valor de otra variable`() {
        val assignment = createAssignment("x", createVariableExpression("y"))
        val result = FormatterImplementation(FormattingRules(allAssignmentRules)).format(assignment)

        assertEquals("x = y;", result)
    }

    @Test
    fun `asigna una operacion entre variable y numero`() {
        val assignment =
            createAssignment(
                "x",
                createOperationExpression(createVariableExpression("y"), createNumberLiteralExpression(3), PrintScriptOperator.SUM),
            )
        val result = FormatterImplementation(FormattingRules(allAssignmentRules)).format(assignment)

        assertEquals("x = y + 3;", result)
    }

    // ---- Grupo C: variaciones de identificador (todas las reglas activas) ----

    @Test
    fun `identificador de una sola letra`() {
        val assignment = createAssignment("a", createNumberLiteralExpression(1))
        val result = FormatterImplementation(FormattingRules(allAssignmentRules)).format(assignment)

        assertEquals("a = 1;", result)
    }

    @Test
    fun `identificador en snake_case`() {
        val assignment = createAssignment("mi_variable", createNumberLiteralExpression(1))
        val result = FormatterImplementation(FormattingRules(allAssignmentRules)).format(assignment)

        assertEquals("mi_variable = 1;", result)
    }

    @Test
    fun `identificador con numero al final`() {
        val assignment = createAssignment("variable2", createNumberLiteralExpression(1))
        val result = FormatterImplementation(FormattingRules(allAssignmentRules)).format(assignment)

        assertEquals("variable2 = 1;", result)
    }
}
