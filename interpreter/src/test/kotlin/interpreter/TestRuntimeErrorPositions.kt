package interpreter

import ast.AST
import ast.Expression
import ast.Program
import domain.Failure
import domain.NumType
import domain.Position
import domain.StrType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TestRuntimeErrorPositions {
    private val interpreter = InterpreterImpl(testSemantics)
    private val dummyIO =
        InterpreterIO(
            emitter = {},
            provider = { "" },
        )

    @Test
    fun `runtime error captures AST statement position on undeclared variable`() {
        val program =
            Program(
                trees =
                    listOf(
                        AST.AssignmentStatement(
                            id = "undeclaredVar",
                            value = Expression.Literal("10", NumType),
                            start = Position(4, 2),
                            end = Position(4, 25),
                        ),
                    ),
                start = Position(4, 2),
                end = Position(4, 25),
            )

        val result = interpreter.execute(program, dummyIO, null)
        assertTrue(result is Failure)
        val error = (result as Failure).value
        assertEquals("Variable doesn't exist", error.getMessage())
        assertEquals(Position(4, 2), error.start)
        assertEquals(Position(4, 25), error.end)
    }

    @Test
    fun `runtime error captures AST statement position on uninitialized variable`() {
        val program =
            Program(
                trees =
                    listOf(
                        AST.DeclarationStatement(
                            id = "x",
                            type = NumType,
                            mutable = true,
                            value = null,
                            start = Position(1, 1),
                            end = Position(1, 18),
                        ),
                        AST.ExpressionStatement(
                            expression =
                                Expression.Call(
                                    name = "println",
                                    args = listOf(Expression.Variable("x")),
                                ),
                            start = Position(3, 5),
                            end = Position(3, 20),
                        ),
                    ),
                start = Position(1, 1),
                end = Position(3, 20),
            )

        val result = interpreter.execute(program, dummyIO, null)
        assertTrue(result is Failure)
        val error = (result as Failure).value
        assertEquals("Variable is not initialized", error.getMessage())
        assertEquals(Position(3, 5), error.start)
        assertEquals(Position(3, 20), error.end)
    }

    @Test
    fun `runtime error captures AST statement position on type mismatch`() {
        val program =
            Program(
                trees =
                    listOf(
                        AST.DeclarationStatement(
                            id = "x",
                            type = NumType,
                            mutable = true,
                            value = Expression.Literal("notANumber", StrType),
                            start = Position(7, 1),
                            end = Position(7, 35),
                        ),
                    ),
                start = Position(7, 1),
                end = Position(7, 35),
            )

        val result = interpreter.execute(program, dummyIO, null)
        assertTrue(result is Failure)
        val error = (result as Failure).value
        assertEquals("That variable exists with a different type", error.getMessage())
        assertEquals(Position(7, 1), error.start)
        assertEquals(Position(7, 35), error.end)
    }
}
