package linter.rules

import ast.AST
import versionFactory.v1_0.Operators
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestPrintlnArgumentRule {
    // --- Should pass (no warning) ---

    @Test
    fun numberLiteral() {
        val arg = createLiteralExpression(42)
        testNoWarning(createPrintln(arg))
    }

    @Test
    fun stringLiteral() {
        val arg = createLiteralExpression("hello world")
        testNoWarning(createPrintln(arg))
    }

    @Test
    fun variableIdentifier() {
        val arg = createVariableExpression("myVar")
        testNoWarning(createPrintln(arg))
    }

    @Test
    fun emptyArguments() {
        testNoWarning(createPrintln())
    }

    // --- Should fail (warning) ---

    @Test
    fun arithmeticOperationWithNumbers() {
        val operation =
            createOperationExpression(
                left = createLiteralExpression(1),
                right = createLiteralExpression(2),
                operator = Operators.SUM,
            )
        testWarning(createPrintln(operation))
    }

    @Test
    fun operationWithVariableAndLiteral() {
        val operation =
            createOperationExpression(
                left = createVariableExpression("x"),
                right = createLiteralExpression(10),
                operator = Operators.SUM,
            )
        testWarning(createPrintln(operation))
    }

    @Test
    fun operationWithTwoVariables() {
        val operation =
            createOperationExpression(
                left = createVariableExpression("a"),
                right = createVariableExpression("b"),
                operator = Operators.MULTIPLY,
            )
        testWarning(createPrintln(operation))
    }

    @Test
    fun stringConcatenation() {
        val operation =
            createOperationExpression(
                left = createLiteralExpression("Hello, "),
                right = createVariableExpression("name"),
                operator = Operators.SUM,
            )
        testWarning(createPrintln(operation))
    }

    @Test
    fun complexNestedOperation() {
        val innerSum =
            createOperationExpression(
                left = createVariableExpression("a"),
                right = createVariableExpression("b"),
                operator = Operators.SUM,
            )
        val outerMultiply =
            createOperationExpression(
                left = innerSum,
                right = createLiteralExpression(5),
                operator = Operators.MULTIPLY,
            )
        testWarning(createPrintln(outerMultiply))
    }

    // --- AST node type coverage ---

    @Test
    fun declarationNodeWithOperationIsIgnored() {
        val operation =
            createOperationExpression(
                left = createLiteralExpression(5),
                right = createLiteralExpression(5),
                operator = Operators.SUM,
            )
        val ast = createDeclaration(name = "x", value = operation)
        testNoWarning(ast)
    }

    @Test
    fun assignmentNodeWithOperationIsIgnored() {
        val operation =
            createOperationExpression(
                left = createLiteralExpression(1),
                right = createLiteralExpression(2),
                operator = Operators.SUM,
            )
        val ast = createAssignment(name = "x", value = operation)
        testNoWarning(ast)
    }

    // --- Helpers ---

    private fun testWarning(ast: AST) {
        val rule = PrintlnArgumentRule()
        val result = rule.apply(ast)
        assertTrue(result != null, "Expected warning for expression argument in println, but got none")
    }

    private fun testNoWarning(ast: AST) {
        val rule = PrintlnArgumentRule()
        val result = rule.apply(ast)
        assertEquals(result, null, "Expected no warning, but got: $result")
    }
}
