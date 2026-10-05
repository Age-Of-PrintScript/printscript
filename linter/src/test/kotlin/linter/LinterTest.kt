package linter

import linter.cases.LinterErrorCases
import linter.cases.LinterSuccessCases
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
    @org.junit.jupiter.api.Test
    fun `reusable linter receives version and config for each analysis`() {
        val linter = Linter.create()
        val snakeCaseConfig =
            """
            {
              "rules": [
                {
                  "name": "identifier-format",
                  "enabled": true,
                  "params": { "convention": "snake_case" }
                }
              ]
            }
            """.trimIndent()

        assertEquals(1, linter.analyse("println(1 + 2);", "1.0").size)
        assertTrue(linter.analyse("let snake_case: string = \"ok\";", "1.1", snakeCaseConfig).isEmpty())
    }

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
