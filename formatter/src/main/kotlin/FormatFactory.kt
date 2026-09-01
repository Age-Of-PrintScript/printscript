import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class FormatFactory(
    private val json: JsonObject,
) {
    private val rulesByKey: Map<String, JsonElement> =
        json["rules"]!!
            .jsonArray
            .flatMap { it.jsonObject.entries }
            .associate { it.key to it.value } // asocio el nombre con el valor elemental del value

    private fun buildRules(keys: List<String>): FormattingRules {
        val formatRules =
            keys.mapNotNull { key ->
                // para cada nombre de regla, le mapeo su formatRule
                // la manera que tiene kt de hacer un 'continue' dentro de una lambda. Devolver solo esa iteración, y no la funcion entera
                val value = rulesByKey[key] ?: return@mapNotNull null
                ruleFactoryMap[key]?.create(value)
            }
        return FormattingRules(formatRules)
    }

    fun create(): Map<String, ASTFormatter> {
        val declarationFormatter =
            FormatterImplementation(
                buildRules(listOf("space before colon", "space after colon", "spaces around assign")),
            )
        val assignmentFormatter =
            FormatterImplementation(
                buildRules(listOf("spaces around assign")),
            )
        val callFormatter =
            FormatterImplementation(
                buildRules(listOf("lines before call")),
            )
        return mapOf(
            "declaration" to declarationFormatter,
            "assignment" to assignmentFormatter,
            "call" to callFormatter,
        )
    }
}
