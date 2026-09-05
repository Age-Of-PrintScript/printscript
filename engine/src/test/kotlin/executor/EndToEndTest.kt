package executor

import executor.cases.FailedPrograms
import executor.cases.SuccessfulPrograms
import executor.cases.ValidationCases
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

data class SuccessCase(
    val name: String,
    val input: String,
    val expectedOutputs: List<String>,
)

data class FailureCase(
    val name: String,
    val input: String,
)

class EndToEndTest {
    private val engine = Engine()

    @TestFactory
    fun `successful program executions`(): List<DynamicNode> =
        SuccessfulPrograms.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectExecution(engine, case.input, case.expectedOutputs)
            }
        }

    @TestFactory
    fun `failed program executions`(): List<DynamicNode> =
        FailedPrograms.cases().map { case ->
            dynamicTest(case.name) {
                assertFailedExecution(engine, case.input)
            }
        }

    @TestFactory
    fun `successful validations`(): List<DynamicNode> =
        ValidationCases.successfulCases().map { case ->
            dynamicTest(case.name) {
                assertCorrectValidation(engine, case.input)
            }
        }

    @TestFactory
    fun `failed validations`(): List<DynamicNode> =
        ValidationCases.failedCases().map { case ->
            dynamicTest(case.name) {
                assertFailedValidation(engine, case.input)
            }
        }

    @Test
    fun `sequential executions preserve execution context`() {
        val logger1 = TestLogger()
        val result1 = engine.execute("let x: number = 42;", logger1)
        assertEquals(ExitCode.SUCCESS, result1.exitCode)

        val logger2 = TestLogger()
        val result2 = engine.execute("println(x);", logger2, result1.context)
        assertEquals(ExitCode.SUCCESS, result2.exitCode)
        assertEquals(listOf("42.0"), logger2.getPrints())
    }
}
