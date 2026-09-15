package linter

import ast.AST
import ast.Expression
import domain.Error
import domain.NumType
import domain.Position
import domain.StrType
import linter.rules.CallArgumentRule
import linter.rules.IdentifierFormatRule
import kotlin.test.Test
import kotlin.test.assertEquals

class TestWarningPositions {
    @Test
    fun `warning from error uses error start position`() {
        val error =
            object : Error {
                override fun getMessage(): String = "Test error"

                override val start: Position = Position(5, 12)
                override val end: Position = Position(5, 20)
            }

        val warning = Warning.fromError(error)
        assertEquals("Test error", warning.message)
        assertEquals(Position(5, 12), warning.position)
    }

    @Test
    fun `warning from error with null start falls back to START`() {
        val error =
            object : Error {
                override fun getMessage(): String = "Fallback error"
            }

        val warning = Warning.fromError(error)
        assertEquals("Fallback error", warning.message)
        assertEquals(Position.START, warning.position)
    }

    @Test
    fun `identifier format rule uses AST start position`() {
        val rule = IdentifierFormatRule(IdentifierConvention.CAMEL_CASE)
        val ast =
            AST.DeclarationStatement(
                id = "Invalid_Name",
                type = StrType,
                mutable = false,
                value = null,
                start = Position(3, 1),
                end = Position(3, 30),
            )

        val warning = rule.apply(ast)
        assertEquals(Position(3, 1), warning?.position)
    }

    @Test
    fun `call argument rule uses AST start position`() {
        val rule = CallArgumentRule("println")
        val ast =
            AST.ExpressionStatement(
                expression =
                    Expression.Call(
                        name = "println",
                        args =
                            listOf(
                                Expression.Operation(
                                    left = Expression.Literal("1", NumType),
                                    operator = versionfactory.v1_0.Operators.SUM,
                                    right = Expression.Literal("2", NumType),
                                ),
                            ),
                    ),
                start = Position(7, 5),
                end = Position(7, 20),
            )

        val warning = rule.apply(ast)
        assertEquals(Position(7, 5), warning?.position)
    }

    @Test
    fun `linter end-to-end reports accurate positions`() {
        val linter = Linter.createDefault("1.0")
        val source =
            """
            let validVar: number = 10;
            let Invalid_Var: string = "hello";
            println(1 + 2);
            """.trimIndent()

        val warnings = linter.analyse(source)
        assertEquals(2, warnings.size)
        // Invalid_Var is on line 2, col 1
        assertEquals(Position(2, 1), warnings[0].position)
        // println(1 + 2) is on line 3, col 1
        assertEquals(Position(3, 1), warnings[1].position)
    }
}
