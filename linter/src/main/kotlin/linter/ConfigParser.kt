package linter

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.File
import java.io.InputStream

@Serializable
internal data class LinterConfig(
    val rules: List<RuleConfigEntry>,
)

@Serializable
internal data class RuleConfigEntry(
    val name: String,
    val enabled: Boolean = true,
    val params: JsonObject = JsonObject(emptyMap()), // resto de los campos, específicos de cada regla
)

internal class ConfigParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(configFile: File): RulesConfig = parse(configFile.inputStream())

    fun parse(inputStream: InputStream): RulesConfig {
        val content = inputStream.bufferedReader().use { it.readText() }
        return parse(content)
    }

    fun parse(jsonContent: String): RulesConfig {
        val config = deserializeConfigJson(jsonContent)
        val rules = buildRules(config)
        return RulesConfig(rules)
    }

    fun parseOrDefault(customConfigStream: InputStream?): RulesConfig = customConfigStream?.let { parse(it) } ?: parseDefault()

    fun parseDefault(): RulesConfig {
        val defaultStream =
            javaClass.classLoader.getResourceAsStream("config.json")
                ?: javaClass.getResourceAsStream("/config.json")
                ?: error("Default linter config 'config.json' not found in resources")
        return parse(defaultStream)
    }

    private fun buildRules(config: LinterConfig): List<LinterRule> =
        config.rules
            .filter { it.enabled }
            .map { entry -> RuleRegistry.build(entry) }

    private fun deserializeConfigJson(jsonContent: String): LinterConfig = json.decodeFromString<LinterConfig>(jsonContent)
}
