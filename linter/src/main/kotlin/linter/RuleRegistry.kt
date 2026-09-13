package linter

import linter.rules.IdentifierFormatRuleFactory
import linter.rules.PrintlnArgumentRuleFactory
import linter.rules.ReadInputArgumentRuleFactory

internal object RuleRegistry {
    private val v1_0Factories: Map<String, LinterRuleFactory> =
        listOf(
            PrintlnArgumentRuleFactory,
            IdentifierFormatRuleFactory,
        ).associateBy { it.ruleName }

    private val v1_1Factories: Map<String, LinterRuleFactory> =
        v1_0Factories +
            listOf(
                ReadInputArgumentRuleFactory,
            ).associateBy { it.ruleName }

    fun getFactoriesForVersion(version: String): Map<String, LinterRuleFactory> =
        when (version) {
            "1.0" -> v1_0Factories
            "1.1" -> v1_1Factories
            else -> throw IllegalArgumentException("Unsupported version: $version")
        }

    fun build(
        entry: RuleConfigEntry,
        version: String,
    ): LinterRule {
        val factories = getFactoriesForVersion(version)
        val factory =
            factories[entry.name]
                ?: throw IllegalArgumentException("Rule '${entry.name}' is not supported in version $version")
        return factory.fromConfig(entry.params)
    }
}
