package formatter

import formatter.formatrules.FormatRule
import formatter.formatrules.LineBeforeCallRule
import formatter.formatrules.SpaceAfterColonRule
import formatter.formatrules.SpaceBeforeColonRule
import formatter.formatrules.SpacesAroundAssignRule
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

interface RuleFactory{
    fun create(value: JsonElement): FormatRule
}

class SpaceAfterColonFactory: RuleFactory{
    override fun  create(value: JsonElement): SpaceAfterColonRule {
        return SpaceAfterColonRule(value.jsonPrimitive.boolean)
    }
}

class SpaceBeforeColonFactory: RuleFactory{
    override fun create(value: JsonElement): SpaceBeforeColonRule {
        return SpaceBeforeColonRule(value.jsonPrimitive.boolean)
    }
}
class SpaceAroundAssignFactory: RuleFactory{
    override fun create(value: JsonElement): SpacesAroundAssignRule {
        return SpacesAroundAssignRule(value.jsonPrimitive.boolean)
    }
}
class LinesBeforeCallFactory: RuleFactory{
    override fun create(value: JsonElement): LineBeforeCallRule{
        return LineBeforeCallRule(value.jsonPrimitive.int)
    }
}



val mapa = mapOf<String, RuleFactory>(
    "space before colon" to SpaceBeforeColonFactory(),
    "space after colon" to SpaceAfterColonFactory(),
    "spaces around assign" to SpaceAroundAssignFactory(),
    "lines before call" to LinesBeforeCallFactory()
    )