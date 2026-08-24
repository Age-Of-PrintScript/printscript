package linter

import java.io.File


import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

@Serializable
data class LinterConfig(
    val rules: List<RuleConfigEntry>
)

@Serializable
data class RuleConfigEntry(
    val name: String,
    val enabled: Boolean = true,
    val params: JsonObject = JsonObject(emptyMap()) // resto de los campos, específicos de cada regla
)

class ConfigParser {
    private val json = Json { ignoreUnknownKeys = true }
    fun parse(configFile: File): RulesConfig {
        val config = deserializeConfigJson(configFile)
        val rules = buildRules(config)
        return RulesConfig(rules)
    }

    private fun buildRules(config: LinterConfig): List<LinterRule> = config.rules
        .filter { it.enabled }
        .map { entry -> RuleRegistry.build(entry) }

    private fun deserializeConfigJson(configFile: File): LinterConfig =
        json.decodeFromString<LinterConfig>(configFile.readText())
}