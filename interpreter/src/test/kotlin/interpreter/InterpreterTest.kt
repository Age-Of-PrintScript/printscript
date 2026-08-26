package interpreter

import ast.Program
import domain.Error
import interpreter.cases.failure.FAILURE_CASES
import interpreter.cases.success.SUCCESS_CASES
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

data class SuccessCase(
    val name: String,
    val program: Program,
    val expectedEnv: RuntimeEnvironment,
    val expectedEvents: RuntimeEvents,
)

data class FailureCase(
    val name: String,
    val program: Program,
    val expectedFailure: Error,
)

class InterpreterTest {
    private val interpreter = InterpreterImpl()

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
}
