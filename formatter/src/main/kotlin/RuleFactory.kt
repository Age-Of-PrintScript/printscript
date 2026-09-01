import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

interface RuleFactory {
    fun create(value: JsonElement): FormatRule
}

class SpaceAfterColonFactory : RuleFactory {
    override fun create(value: JsonElement): SpaceAfterColonRule = SpaceAfterColonRule(value.jsonPrimitive.boolean)
}

class SpaceAroundAssignFactory : RuleFactory {
    override fun create(value: JsonElement): SpacesAroundAssignRule = SpacesAroundAssignRule(value.jsonPrimitive.boolean)
}

class LinesBeforeCallFactory : RuleFactory {
    override fun create(value: JsonElement): LineBeforeCallRule = LineBeforeCallRule(value.jsonPrimitive.int)
}

class SpaceBeforeColonFactory : RuleFactory {
    override fun create(value: JsonElement): SpaceBeforeColonRule = SpaceBeforeColonRule(value.jsonPrimitive.boolean)
}

val ruleFactoryMap =
    mapOf(
        "space before colon" to SpaceBeforeColonFactory(),
        "space after colon" to SpaceAfterColonFactory(),
        "spaces around assign" to SpaceAroundAssignFactory(),
        "lines before call" to LinesBeforeCallFactory(),
    )
