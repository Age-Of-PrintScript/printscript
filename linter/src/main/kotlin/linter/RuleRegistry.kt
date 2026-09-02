package linter

import linter.rules.IdentifierFormatRuleFactory
import linter.rules.PrintlnArgumentRuleFactory

internal object RuleRegistry {
    private val factories: Map<String, LinterRuleFactory> =
        listOf(
            PrintlnArgumentRuleFactory,
            IdentifierFormatRuleFactory,
        ).associateBy { it.ruleName }

    fun build(entry: RuleConfigEntry): LinterRule {
        val factory =
            factories[entry.name]
                ?: throw IllegalArgumentException("Unknown rule: ${entry.name}")
        return factory.fromConfig(entry.params)
    }
}
