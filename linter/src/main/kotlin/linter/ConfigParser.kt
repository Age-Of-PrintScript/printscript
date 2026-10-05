package linter

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

@Serializable
internal data class LinterConfig(
    val rules: List<RuleConfigEntry>,
)

@Serializable
internal data class RuleConfigEntry(
    val name: String,
    val enabled: Boolean = true,
    val params: JsonObject = JsonObject(emptyMap()),
)

internal class ConfigParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun resolve(
        jsonContent: String?,
        version: String,
    ): RulesConfig {
        val content = jsonContent ?: defaultConfigContent()
        val config = json.decodeFromString<LinterConfig>(content)
        return RulesConfig(buildRules(config, version))
    }

    private fun defaultConfigContent(): String {
        val defaultStream =
            javaClass.classLoader.getResourceAsStream("config.json")
                ?: javaClass.getResourceAsStream("/config.json")
                ?: error("Default linter config 'config.json' not found in resources")
        return defaultStream.bufferedReader().use { it.readText() }
    }

    private fun buildRules(
        config: LinterConfig,
        version: String,
    ): List<LinterRule> =
        config.rules
            .filter { it.enabled }
            .map { entry -> RuleRegistry.build(entry, version) }
}
