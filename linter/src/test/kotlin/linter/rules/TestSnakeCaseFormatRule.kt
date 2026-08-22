package linter.rules

import ast.AST
import ast.ASTDataType
import ast.ASTIdentifier
import ast.Expression
import domain.PrintScriptType
import domain.PrintScriptValue
import linter.IdentifierConvention
import kotlin.test.Test
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
    fun assignmentNodeIsIgnored() {
        val rule = IdentifierFormatRule(IdentifierConvention.SNAKE_CASE)
        val ast = AST.Assignment(
            id = ASTIdentifier("camelCase"),
            value = Expression.Literal(PrintScriptValue.NumberLiteral(1))
        )
        val result = rule.apply(ast)
        assertTrue(result.isEmpty)
    }

    // --- Helpers ---

    private fun testWarning(identifier: String) {
        val rule = IdentifierFormatRule(IdentifierConvention.SNAKE_CASE)
        val ast = buildDeclaration(identifier)
        val result = rule.apply(ast)
        assertTrue(result.isPresent, "Expected warning for '$identifier' but got none")
    }

    private fun testNoWarning(identifier: String) {
        val rule = IdentifierFormatRule(IdentifierConvention.SNAKE_CASE)
        val ast = buildDeclaration(identifier)
        val result = rule.apply(ast)
        assertTrue(result.isEmpty, "Expected no warning for '$identifier' but got one")
    }

    private fun buildDeclaration(id: String): AST {
        return AST.Declaration(
            id = ASTIdentifier(id),
            type = ASTDataType(PrintScriptType.NUMBER),
            value = Expression.Literal(PrintScriptValue.NumberLiteral(5))
        )
    }
}
