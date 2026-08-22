package linter

import interpreter.RuntimeEvents
import kotlinx.serialization.json.JsonObject
import linter.rules.PrintlnArgumentRuleFactory

object RuleRegistry {
    private val factories: Map<String, LinterRuleFactory> = listOf(
        PrintlnArgumentRuleFactory
    ).associateBy { it.ruleName }

    fun build(entry: RuleConfigEntry): LinterRule {
        val factory = factories[entry.name]
            ?: throw RuntimeException("Unknown rule: $name")
        return factory.fromConfig(entry.params)
    }
}