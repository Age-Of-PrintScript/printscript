package formatter.astformatterfactory

import formatter.ASTFormatter
import formatter.FormatterImplementation
import formatter.formatrules.FormattingRules
import formatter.mapa
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class FormatFactory(private val json: JsonObject) {

    private val rulesByKey: Map<String, JsonElement> =
        json["rules"]!!.jsonArray
            .flatMap { it.jsonObject.entries }
            .associate { it.key to it.value }

    private fun buildRules(keys: List<String>): FormattingRules {
        val formatRules = keys.mapNotNull { key ->
            val value = rulesByKey[key] ?: return@mapNotNull null
            mapa[key]?.create(value)
        }
        return FormattingRules(formatRules)
    }

    fun create(): List<ASTFormatter> {
        val declarationFormatter = FormatterImplementation(
            buildRules(listOf("space before colon", "space after colon", "spaces around assign"))
        )
        val assignmentFormatter = FormatterImplementation(
            buildRules(listOf("spaces around assign"))
        )
        val callFormatter = FormatterImplementation(
            buildRules(listOf("lines before call"))
        )
        return listOf(declarationFormatter, assignmentFormatter, callFormatter)
    }
}