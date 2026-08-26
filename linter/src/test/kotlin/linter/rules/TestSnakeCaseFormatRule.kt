package linter.rules

import linter.IdentifierConvention
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestSnakeCaseFormatRule {
    // --- Should pass (no warning) ---

    @Test
    fun singleChar() {
        testNoWarning("x")
    }

    @Test
    fun simpleSnakeCase() {
        testNoWarning("snake_case")
    }

    @Test
    fun snakeCaseWithNumber() {
        testNoWarning("my_var_2")
    }

    @Test
    fun singleWordLowercase() {
        testNoWarning("hello")
    }

    @Test
    fun numberInMiddleSegment() {
        testNoWarning("my2var")
    }

    // --- Should fail (warning) ---

    @Test
    fun camelCaseIdentifier() {
        testWarning("camelCase")
    }

    @Test
    fun allUpperCase() {
        testWarning("MAYUS")
    }

    @Test
    fun pascalCase() {
        testWarning("MyVar")
    }

    @Test
    fun leadingUnderscore() {
        testWarning("_leading")
    }

    @Test
    fun trailingUnderscore() {
        testWarning("trailing_")
    }

    @Test
    fun doubleUnderscore() {
        testWarning("double__under")
    }

    @Test
    fun upperCaseInSegment() {
        testWarning("my_Var")
    }

    // --- AST node type coverage ---

    @Test
    fun assignmentNodeIsChecked() {
        val rule = IdentifierFormatRule(IdentifierConvention.SNAKE_CASE)
        val ast = createAssignment(name = "camelCase")
        val result = rule.apply(ast)
        assertTrue(result != null, "Expected warning for 'camelCase' in Assignment node")
    }

    // --- Helpers ---

    private fun testWarning(identifier: String) {
        val rule = IdentifierFormatRule(IdentifierConvention.SNAKE_CASE)
        val ast = createDeclaration(name = identifier)
        val result = rule.apply(ast)
        assertTrue(result != null, "Expected warning for '$identifier' but got none")
    }

    private fun testNoWarning(identifier: String) {
        val rule = IdentifierFormatRule(IdentifierConvention.SNAKE_CASE)
        val ast = createDeclaration(name = identifier)
        val result = rule.apply(ast)
        assertEquals(result, null, "Expected no warning for '$identifier' but got one")
    }
}
