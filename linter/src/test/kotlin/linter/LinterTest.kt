package linter

import linter.cases.LinterErrorCases
import linter.cases.LinterSuccessCases
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal data class LinterTestCase(
    val name: String,
    val source: String,
    val expectedWarningsCount: Int,
    val version: String = "1.0",
    val configJson: String? = null,
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

    @org.junit.jupiter.api.Test
    fun `version-specific rule is accepted only by its supported version`() {
        val linter = Linter.create()
        val readInputRuleConfig =
            """
            {
              "rules": [
                {
                  "name": "readInput-no-expression",
                  "enabled": true
                }
              ]
            }
            """.trimIndent()
        val source = "let input: string = readInput(\"Name: \");"

        assertTrue(linter.analyse(source, "1.1", readInputRuleConfig).isEmpty())
        assertThrows(IllegalArgumentException::class.java) {
            linter.analyse(source, "1.0", readInputRuleConfig)
        }
    }

    @TestFactory
    fun `successful linter analysis cases`(): List<DynamicNode> {
        val linter = Linter.create()
        return LinterSuccessCases.cases().map { case ->
            dynamicTest(case.name) {
                val warnings = linter.analyse(case.source, case.version, case.configJson)
                assertEquals(
                    case.expectedWarningsCount,
                    warnings.size,
                    "Expected ${case.expectedWarningsCount} warnings but got ${warnings.size}: $warnings",
                )
            }
        }
    }

    @TestFactory
    fun `error and edge cases in linter analysis`(): List<DynamicNode> {
        val linter = Linter.create()
        return LinterErrorCases.cases().map { case ->
            dynamicTest(case.name) {
                val warnings = linter.analyse(case.source, case.version, case.configJson)
                assertEquals(
                    case.expectedWarningsCount,
                    warnings.size,
                    "Expected ${case.expectedWarningsCount} warnings but got ${warnings.size}: $warnings",
                )
            }
        }
    }
}
