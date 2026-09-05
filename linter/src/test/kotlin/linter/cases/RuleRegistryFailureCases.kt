package linter.cases

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import linter.RuleConfigEntry
import linter.RuleRegistryFailureCase

internal object RuleRegistryFailureCases {
    fun cases(): List<RuleRegistryFailureCase> =
        listOf(
            RuleRegistryFailureCase(
                name = "build unknown rule name throws IllegalArgumentException",
                entry =
                    RuleConfigEntry(
                        name = "unknown-rule",
                        enabled = true,
                    ),
                expectedException = IllegalArgumentException::class,
            ),
            RuleRegistryFailureCase(
                name = "identifier-format missing convention param throws IllegalArgumentException",
                entry =
                    RuleConfigEntry(
                        name = "identifier-format",
                        enabled = true,
                        params = JsonObject(emptyMap()),
                    ),
                expectedException = IllegalArgumentException::class,
            ),
            RuleRegistryFailureCase(
                name = "identifier-format unknown convention value throws IllegalArgumentException",
                entry =
                    RuleConfigEntry(
                        name = "identifier-format",
                        enabled = true,
                        params = JsonObject(mapOf("convention" to JsonPrimitive("kebab-case"))),
                    ),
                expectedException = IllegalArgumentException::class,
            ),
        )
}
