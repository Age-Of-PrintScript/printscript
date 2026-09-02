package linter

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class RuleRegistryTest {
    @Test
    fun testBuildIdentifierFormatRule() {
        val entry =
            RuleConfigEntry(
                name = "identifier-format",
                enabled = true,
                params = JsonObject(mapOf("convention" to JsonPrimitive("camelCase"))),
            )
        val rule = RuleRegistry.build(entry)
        assertNotNull(rule)
    }

    @Test
    fun testBuildPrintlnRule() {
        val entry =
            RuleConfigEntry(
                name = "println-no-expression",
                enabled = true,
                params = JsonObject(emptyMap()),
            )
        val rule = RuleRegistry.build(entry)
        assertNotNull(rule)
    }

    @Test
    fun testBuildUnknownRuleThrowsException() {
        val entry =
            RuleConfigEntry(
                name = "unknown-rule",
                enabled = true,
            )
        assertFailsWith<IllegalArgumentException> {
            RuleRegistry.build(entry)
        }
    }

    @Test
    fun testMissingConventionParamThrowsException() {
        val entry =
            RuleConfigEntry(
                name = "identifier-format",
                enabled = true,
                params = JsonObject(emptyMap()),
            )
        assertFailsWith<IllegalArgumentException> {
            RuleRegistry.build(entry)
        }
    }

    @Test
    fun testUnknownConventionParamThrowsException() {
        val entry =
            RuleConfigEntry(
                name = "identifier-format",
                enabled = true,
                params = JsonObject(mapOf("convention" to JsonPrimitive("invalid_convention"))),
            )
        assertFailsWith<IllegalArgumentException> {
            RuleRegistry.build(entry)
        }
    }
}
