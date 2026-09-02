package linter

import linter.cases.RuleRegistryFailureCases
import linter.cases.RuleRegistrySuccessCases
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.reflect.KClass

internal data class RuleRegistrySuccessCase(
    val name: String,
    val entry: RuleConfigEntry,
    val expectedClass: KClass<out LinterRule>,
)

internal data class RuleRegistryFailureCase(
    val name: String,
    val entry: RuleConfigEntry,
    val expectedException: KClass<out Throwable>,
)

internal class RuleRegistryTest {
    @TestFactory
    fun `successful rule creations`(): List<DynamicNode> =
        RuleRegistrySuccessCases.cases().map { case ->
            dynamicTest(case.name) {
                val rule = RuleRegistry.build(case.entry)
                assertNotNull(rule)
                assertTrue(
                    case.expectedClass.isInstance(rule),
                    "Expected ${case.expectedClass.simpleName} but got ${rule::class.simpleName}",
                )
            }
        }

    @TestFactory
    fun `failure rule creations`(): List<DynamicNode> =
        RuleRegistryFailureCases.cases().map { case ->
            dynamicTest(case.name) {
                assertThrows(case.expectedException.java) {
                    RuleRegistry.build(case.entry)
                }
            }
        }
}
