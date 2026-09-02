package formatter

import domain.Either
import domain.Error
import domain.Failure
import domain.Success
import domain.getOrReturn
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class FormatterFactory( // implementación fea, pero que por ahora anda. Lo dejo asi para esta integración
    private val json: JsonObject,
) {
    private fun extractRulesByKey(): Either<Error, Map<String, JsonElement>> {
        // aca manejo null pointers, si no encuentra el campo 'rules', devuelvo un failure
        val rulesElement = json["rules"] ?: return Failure(FormattingError.INVALID_JSON)

        // as? es casteo seguro de kt, si las reglas no son de tipo JsonArray, maneja el null devolviendo un failure en vez de tirar ClassCastException
        val rulesArray = rulesElement as? JsonArray ?: return Failure(FormattingError.INVALID_JSON)

        val rulesByKey =
            rulesArray
                .flatMap { it.jsonObject.entries } // aplano todos los jsonObjects a una lista de entries (un jsonObject puede tener 1 o mas reglas dentro)
                .associate { it.key to it.value } // asocio las keys con los valos de cada objetito

        return Success(rulesByKey)
    }

    private fun buildConfigurableRules(
        keys: List<String>,
        rulesByKey: Map<String, JsonElement>,
    ): List<FormatRule> =
        keys.mapNotNull { key ->
            val value = rulesByKey[key] ?: return@mapNotNull null
            ruleFactoryMap[key]?.create(value)
        }

    private fun buildFormatter(
        keys: List<String>,
        rulesByKey: Map<String, JsonElement>,
        fixedRules: List<FormatRule> = emptyList(),
    ): FormatterImplementation = FormatterImplementation(FormattingRules(buildConfigurableRules(keys, rulesByKey) + fixedRules))

    fun constructFormatters(): Either<Error, Map<String, ASTFormatter>> {
        val rulesByKey = extractRulesByKey().getOrReturn { return Failure(it) }
        val fixedRules = listOf(SemiColonAtTheEndRule(true))

        val formatters =
            mapOf(
                "declaration" to
                    buildFormatter(
                        listOf("space before colon", "space after colon", "spaces around assign"),
                        rulesByKey,
                        fixedRules,
                    ),
                "assignment" to
                    buildFormatter(
                        listOf("spaces around assign"),
                        rulesByKey,
                        fixedRules,
                    ),
                "call" to
                    buildFormatter(
                        listOf("lines before call"),
                        rulesByKey,
                        fixedRules,
                    ),
            )

        return Success(formatters)
    }
}
