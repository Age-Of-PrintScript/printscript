package linter

import linter.cases.LinterErrorCases
import linter.cases.LinterSuccessCases
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal data class LinterTestCase(
    val name: String,
    val linterProvider: () -> Linter,
    val source: String,
    val expectedWarningsCount: Int,
)

internal class LinterTest {
    @TestFactory
    fun `successful linter analysis cases`(): List<DynamicNode> =
        LinterSuccessCases.cases().map { case ->
            dynamicTest(case.name) {
                val linter = case.linterProvider()
                val warnings = linter.analyse(case.source)
                assertEquals(
                    case.expectedWarningsCount,
                    warnings.size,
                    "Expected ${case.expectedWarningsCount} warnings but got ${warnings.size}: $warnings",
                )
            }
        }

    @TestFactory
    fun `error and edge cases in linter analysis`(): List<DynamicNode> =
        LinterErrorCases.cases().map { case ->
            dynamicTest(case.name) {
                val linter = case.linterProvider()
                val warnings = linter.analyse(case.source)
                assertEquals(
                    case.expectedWarningsCount,
                    warnings.size,
                    "Expected ${case.expectedWarningsCount} warnings but got ${warnings.size}: $warnings",
                )
            }
        }
}
