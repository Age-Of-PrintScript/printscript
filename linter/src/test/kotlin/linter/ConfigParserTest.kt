package linter

import linter.cases.ConfigParserFailureCases
import linter.cases.ConfigParserSuccessCases
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.reflect.KClass

internal data class ConfigParserSuccessCase(
    val name: String,
    val execute: (ConfigParser) -> RulesConfig,
    val expectedRulesCount: Int,
)

internal data class ConfigParserFailureCase(
    val name: String,
    val execute: (ConfigParser) -> Unit,
    val expectedException: KClass<out Throwable>,
)

internal class ConfigParserTest {
    private val parser = ConfigParser()

    @TestFactory
    fun `successful config parsing`(): List<DynamicNode> =
        ConfigParserSuccessCases.cases().map { case ->
            dynamicTest(case.name) {
                val rulesConfig = case.execute(parser)
                assertEquals(case.expectedRulesCount, rulesConfig.rules.size)
            }
        }

    @TestFactory
    fun `failure config parsing`(): List<DynamicNode> =
        ConfigParserFailureCases.cases().map { case ->
            dynamicTest(case.name) {
                assertThrows(case.expectedException.java) {
                    case.execute(parser)
                }
            }
        }
}
