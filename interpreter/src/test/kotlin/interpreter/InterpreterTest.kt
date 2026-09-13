package interpreter

import ast.Program
import domain.Error
import interpreter.cases.failure.FAILURE_CASES
import interpreter.cases.success.SUCCESS_CASES
import interpreter.environment.RuntimeEnvironment
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

data class SuccessCase(
    val name: String,
    val program: Program,
    val expectedEnv: RuntimeEnvironment,
    val expectedEvents: List<String>,
)

data class FailureCase(
    val name: String,
    val program: Program,
    val expectedFailure: Error,
)

class InterpreterTest {
    private val interpreter = InterpreterImpl(testSemantics)

    @TestFactory
    fun `successful interpreter executions`(): List<DynamicNode> =
        SUCCESS_CASES.map { case ->
            dynamicTest(case.name) {
                assertSuccessCase(interpreter, case)
            }
        }

    @TestFactory
    fun `failure interpreter executions`(): List<DynamicNode> =
        FAILURE_CASES.map { case ->
            dynamicTest(case.name) {
                assertFailureCase(interpreter, case)
            }
        }

    @org.junit.jupiter.api.Test
    fun `readInput assigns input value to variable and passes prompt to provider`() {
        val prompts = mutableListOf<String>()
        val io =
            InterpreterIO(
                emitter = {},
                provider = { prompt ->
                    prompts.add(prompt)
                    "Alice"
                },
            )
        val pos = domain.Position(0, 0)
        val program =
            Program(
                listOf(
                    ast.AST.DeclarationStatement(
                        id = "name",
                        type = domain.StrType,
                        mutable = true,
                        value =
                            ast.Expression.Call(
                                name = "readInput",
                                args = listOf(ast.Expression.Literal("Enter name: ", domain.StrType)),
                            ),
                    ),
                ),
                pos,
                pos,
            )

        val result = interpreter.execute(program, io, null)

        org.junit.jupiter.api.Assertions
            .assertTrue(result is domain.Success)
        val env = (result as domain.Success).value
        org.junit.jupiter.api.Assertions.assertEquals(
            ast.Expression.Literal("Alice", domain.StrType),
            env.getVariableMapWithValues()["name"],
        )
        org.junit.jupiter.api.Assertions
            .assertEquals(listOf("Enter name: "), prompts)
    }

    @org.junit.jupiter.api.Test
    fun `readInput followed by println prints read value in real time`() {
        val prints = mutableListOf<String>()
        val io =
            InterpreterIO(
                emitter = { prints.add(it) },
                provider = { "Bob" },
            )
        val pos = domain.Position(0, 0)
        val program =
            Program(
                listOf(
                    ast.AST.DeclarationStatement(
                        id = "x",
                        type = domain.StrType,
                        mutable = true,
                        value =
                            ast.Expression.Call(
                                name = "readInput",
                                args = listOf(ast.Expression.Literal("prompt: ", domain.StrType)),
                            ),
                    ),
                    ast.AST.ExpressionStatement(
                        ast.Expression.Call(
                            name = "println",
                            args = listOf(ast.Expression.Variable("x")),
                        ),
                    ),
                ),
                pos,
                pos,
            )

        val result = interpreter.execute(program, io, null)

        org.junit.jupiter.api.Assertions
            .assertTrue(result is domain.Success)
        org.junit.jupiter.api.Assertions
            .assertEquals(listOf("Bob"), prints)
    }

    @org.junit.jupiter.api.Test
    fun `multiple sequential readInputs consume inputs in order`() {
        val prints = mutableListOf<String>()
        val inputQueue = ArrayDeque(listOf("Hello", "World"))
        val io =
            InterpreterIO(
                emitter = { prints.add(it) },
                provider = { inputQueue.removeFirst() },
            )
        val pos = domain.Position(0, 0)
        val program =
            Program(
                listOf(
                    ast.AST.DeclarationStatement(
                        id = "a",
                        type = domain.StrType,
                        mutable = true,
                        value = ast.Expression.Call("readInput", listOf(ast.Expression.Literal("A: ", domain.StrType))),
                    ),
                    ast.AST.DeclarationStatement(
                        id = "b",
                        type = domain.StrType,
                        mutable = true,
                        value = ast.Expression.Call("readInput", listOf(ast.Expression.Literal("B: ", domain.StrType))),
                    ),
                    ast.AST.ExpressionStatement(
                        ast.Expression.Call(
                            name = "println",
                            args =
                                listOf(
                                    ast.Expression.Operation(
                                        left = ast.Expression.Variable("a"),
                                        operator = Operators.SUM,
                                        right = ast.Expression.Variable("b"),
                                    ),
                                ),
                        ),
                    ),
                ),
                pos,
                pos,
            )

        val result = interpreter.execute(program, io, null)

        org.junit.jupiter.api.Assertions
            .assertTrue(result is domain.Success)
        org.junit.jupiter.api.Assertions
            .assertEquals(listOf("HelloWorld"), prints)
    }

    @org.junit.jupiter.api.Test
    fun `reassigning variable with readInput updates environment`() {
        val io =
            InterpreterIO(
                emitter = {},
                provider = { "UpdatedValue" },
            )
        val pos = domain.Position(0, 0)
        val program =
            Program(
                listOf(
                    ast.AST.DeclarationStatement(
                        id = "x",
                        type = domain.StrType,
                        mutable = true,
                        value = ast.Expression.Literal("Initial", domain.StrType),
                    ),
                    ast.AST.AssignmentStatement(
                        id = "x",
                        value = ast.Expression.Call("readInput", listOf(ast.Expression.Literal("New: ", domain.StrType))),
                    ),
                ),
                pos,
                pos,
            )

        val result = interpreter.execute(program, io, null)

        org.junit.jupiter.api.Assertions
            .assertTrue(result is domain.Success)
        val env = (result as domain.Success).value
        org.junit.jupiter.api.Assertions.assertEquals(
            ast.Expression.Literal("UpdatedValue", domain.StrType),
            env.getVariableMapWithValues()["x"],
        )
    }
}
