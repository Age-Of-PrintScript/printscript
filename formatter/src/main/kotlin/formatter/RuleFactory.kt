package formatter

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

internal interface RuleFactory {
    fun create(value: JsonElement): FormatRule
}

internal class SpaceAfterColonFactory : RuleFactory {
    override fun create(value: JsonElement): SpaceAfterColonRule = SpaceAfterColonRule(value.jsonPrimitive.boolean)
}

internal class SpaceAroundAssignFactory : RuleFactory {
    override fun create(value: JsonElement): SpacesAroundAssignRule = SpacesAroundAssignRule(value.jsonPrimitive.boolean)
}

internal class LinesBeforeCallFactory : RuleFactory {
    override fun create(value: JsonElement): LineBeforeCallRule = LineBeforeCallRule(value.jsonPrimitive.int)
}

internal class SpaceBeforeColonFactory : RuleFactory {
    override fun create(value: JsonElement): SpaceBeforeColonRule = SpaceBeforeColonRule(value.jsonPrimitive.boolean)
}

internal val ruleFactoryMap =
    mapOf(
        "space before colon" to SpaceBeforeColonFactory(),
        "space after colon" to SpaceAfterColonFactory(),
        "spaces around assign" to SpaceAroundAssignFactory(),
        "lines before call" to LinesBeforeCallFactory(),
    )
