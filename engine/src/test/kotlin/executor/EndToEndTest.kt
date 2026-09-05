package executor

import executor.cases.FailedPrograms
import executor.cases.SuccessfulAssignments
import executor.cases.SuccessfulCalls
import executor.cases.SuccessfulDeclarations
import executor.cases.SuccessfulEdgeCases
import executor.cases.SuccessfulExpressions
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
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
    fun `successful declarations`(): List<DynamicNode> =
        SuccessfulDeclarations.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectExecution(engine, case.input, case.expectedOutputs)
            }
        }

    @TestFactory
    fun `successful assignments`(): List<DynamicNode> =
        SuccessfulAssignments.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectExecution(engine, case.input, case.expectedOutputs)
            }
        }

    @TestFactory
    fun `successful calls`(): List<DynamicNode> =
        SuccessfulCalls.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectExecution(engine, case.input, case.expectedOutputs)
            }
        }

    @TestFactory
    fun `successful expressions`(): List<DynamicNode> =
        SuccessfulExpressions.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectExecution(engine, case.input, case.expectedOutputs)
            }
        }

    @TestFactory
    fun `successful edge cases`(): List<DynamicNode> =
        SuccessfulEdgeCases.cases().map { case ->
            dynamicTest(case.name) {
                assertCorrectExecution(engine, case.input, case.expectedOutputs)
            }
        }

    @TestFactory
    fun `failed programs`(): List<DynamicNode> =
        FailedPrograms.cases().map { case ->
            dynamicTest(case.name) {
                assertFailedExecution(engine, case.input)
            }
        }
}
