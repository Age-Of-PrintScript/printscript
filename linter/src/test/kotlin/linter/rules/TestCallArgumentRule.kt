package linter.rules

import ast.AST
import versionfactory.v1_0.Operators
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestCallArgumentRule {
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

    // --- readInput in Declarations and Assignments ---

    @Test
    fun readInputWithLiteralInDeclaration() {
        val readInputCall = createReadInput(createLiteralExpression("Enter name: "))
        val ast = createDeclaration(name = "x", value = readInputCall)
        testNoWarning(ast, functionName = "readInput")
    }

    @Test
    fun readInputWithVariableInAssignment() {
        val readInputCall = createReadInput(createVariableExpression("prompt"))
        val ast = createAssignment(name = "x", value = readInputCall)
        testNoWarning(ast, functionName = "readInput")
    }

    @Test
    fun readInputWithEmptyArgumentsInDeclaration() {
        val readInputCall = createReadInput()
        val ast = createDeclaration(name = "x", value = readInputCall)
        testNoWarning(ast, functionName = "readInput")
    }

    @Test
    fun readInputWithOperationInDeclarationFails() {
        val operation =
            createOperationExpression(
                left = createLiteralExpression("Hello, "),
                right = createVariableExpression("name"),
                operator = Operators.SUM,
            )
        val readInputCall = createReadInput(operation)
        val ast = createDeclaration(name = "x", value = readInputCall)
        testWarning(ast, functionName = "readInput")
    }

    @Test
    fun readInputWithOperationInAssignmentFails() {
        val operation =
            createOperationExpression(
                left = createLiteralExpression(1),
                right = createLiteralExpression(2),
                operator = Operators.SUM,
            )
        val readInputCall = createReadInput(operation)
        val ast = createAssignment(name = "x", value = readInputCall)
        testWarning(ast, functionName = "readInput")
    }

    // --- Helpers ---

    private fun testWarning(
        ast: AST,
        functionName: String = "println",
    ) {
        val rule = CallArgumentRule(functionName)
        val result = rule.apply(ast)
        assertTrue(result != null, "Expected warning for expression argument in $functionName, but got none")
    }

    private fun testNoWarning(
        ast: AST,
        functionName: String = "println",
    ) {
        val rule = CallArgumentRule(functionName)
        val result = rule.apply(ast)
        assertEquals(result, null, "Expected no warning, but got: $result")
    }
}
