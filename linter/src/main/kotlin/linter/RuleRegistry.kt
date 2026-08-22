package linter

import interpreter.RuntimeEvents
import kotlinx.serialization.json.JsonObject
import linter.rules.PrintlnArgumentRuleFactory

object RuleRegistry {
    private val factories: Map<String, LinterRuleFactory> = listOf(
        PrintlnArgumentRuleFactory
    ).associateBy { it.ruleName }

    fun build(name: String, json: JsonObject): LinterRule {
        val factory = factories[name]
            ?: throw RuntimeException("Unknown rule: $name")
        return factory.fromConfig(json)
    }
}