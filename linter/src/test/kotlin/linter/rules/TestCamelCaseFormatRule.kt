package linter.rules

import linter.IdentifierConvention
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestCamelCaseFormatRule {
    // --- Should pass (no warning) ---

    @Test
    fun singleChar() {
        testNoWarning("x")
    }

    @Test
    fun simpleCamelCase() {
        testNoWarning("myVar")
    }

    @Test
    fun singleWordLowercase() {
        testNoWarning("hello")
    }

    @Test
    fun camelCaseWithNumber() {
        testNoWarning("myVar2Name")
    }

    @Test
    fun numberInMiddle() {
        testNoWarning("my2var")
    }

    @Test
    fun multipleHumps() {
        testNoWarning("thisIsALongName")
    }

    // --- Should fail (warning) ---

    @Test
    fun snakeCaseIdentifier() {
        testWarning("snake_case")
    }

    @Test
    fun allUpperCase() {
        testWarning("ALLCAPS")
    }

    @Test
    fun pascalCase() {
        testWarning("MyVar")
    }

    @Test
    fun underscoreInMiddle() {
        testWarning("my_var")
    }

    @Test
    fun leadingUnderscore() {
        testWarning("_leading")
    }

    @Test
    fun startWithNumber() {
        testWarning("2fast")
    }

    // --- AST node type coverage ---

    @Test
    fun callNodeIsIgnored() {
        val rule = IdentifierFormatRule(IdentifierConvention.CAMEL_CASE)
        val ast = createPrintln(createLiteralExpression("hello"))
        val result = rule.apply(ast)
        assertEquals(result, null, "Expected no warning for Call nodes")
    }

    @Test
    fun assignmentNodeIsChecked() {
        val rule = IdentifierFormatRule(IdentifierConvention.CAMEL_CASE)
        val ast = createAssignment(name = "snake_case")
        val result = rule.apply(ast)
        assertTrue(result != null, "Expected warning for 'snake_case' in Assignment node")
    }

    // --- Helpers ---

    private fun testWarning(identifier: String) {
        val rule = IdentifierFormatRule(IdentifierConvention.CAMEL_CASE)
        val ast = createDeclaration(name = identifier)
        val result = rule.apply(ast)
        assertTrue(result != null, "Expected warning for '$identifier' but got none")
    }

    private fun testNoWarning(identifier: String) {
        val rule = IdentifierFormatRule(IdentifierConvention.CAMEL_CASE)
        val ast = createDeclaration(name = identifier)
        val result = rule.apply(ast)
        assertEquals(result, null, "Expected no warning for '$identifier' but got one")
    }
}
