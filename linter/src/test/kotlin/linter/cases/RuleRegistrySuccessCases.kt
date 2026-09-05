package linter.cases

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import linter.RuleConfigEntry
import linter.RuleRegistrySuccessCase
import linter.rules.IdentifierFormatRule
import linter.rules.PrintlnArgumentRule

internal object RuleRegistrySuccessCases {
    fun cases(): List<RuleRegistrySuccessCase> =
        listOf(
            RuleRegistrySuccessCase(
                name = "build identifier-format with camelCase convention",
                entry =
                    RuleConfigEntry(
                        name = "identifier-format",
                        enabled = true,
                        params = JsonObject(mapOf("convention" to JsonPrimitive("camelCase"))),
                    ),
                expectedClass = IdentifierFormatRule::class,
            ),
            RuleRegistrySuccessCase(
                name = "build identifier-format with snake_case convention",
                entry =
                    RuleConfigEntry(
                        name = "identifier-format",
                        enabled = true,
                        params = JsonObject(mapOf("convention" to JsonPrimitive("snake_case"))),
                    ),
                expectedClass = IdentifierFormatRule::class,
            ),
            RuleRegistrySuccessCase(
                name = "build println-no-expression with empty params",
                entry =
                    RuleConfigEntry(
                        name = "println-no-expression",
                        enabled = true,
                        params = JsonObject(emptyMap()),
                    ),
                expectedClass = PrintlnArgumentRule::class,
            ),
            RuleRegistrySuccessCase(
                name = "build println-no-expression with extra params ignored",
                entry =
                    RuleConfigEntry(
                        name = "println-no-expression",
                        enabled = true,
                        params = JsonObject(mapOf("extra" to JsonPrimitive("ignored"))),
                    ),
                expectedClass = PrintlnArgumentRule::class,
            ),
        )
}
