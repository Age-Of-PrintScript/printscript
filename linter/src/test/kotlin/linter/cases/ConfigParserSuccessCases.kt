package linter.cases

import linter.ConfigParserSuccessCase
import java.io.File

internal object ConfigParserSuccessCases {
    fun cases(): List<ConfigParserSuccessCase> =
        listOf(
            ConfigParserSuccessCase(
                name = "parse default config from resources",
                execute = { parser -> parser.parseDefault() },
                expectedRulesCount = 2,
            ),
            ConfigParserSuccessCase(
                name = "parse or default with null stream falls back to default",
                execute = { parser -> parser.parseOrDefault(null) },
                expectedRulesCount = 2,
            ),
            ConfigParserSuccessCase(
                name = "parse or default with custom stream",
                execute = { parser ->
                    val json =
                        """
                        {
                          "rules": [
                            {
                              "name": "identifier-format",
                              "enabled": true,
                              "params": { "convention": "snake_case" }
                            }
                          ]
                        }
                        """.trimIndent()
                    parser.parseOrDefault(json.byteInputStream())
                },
                expectedRulesCount = 1,
            ),
            ConfigParserSuccessCase(
                name = "parse json string with some rules disabled",
                execute = { parser ->
                    val json =
                        """
                        {
                          "rules": [
                            {
                              "name": "println-no-expression",
                              "enabled": true
                            },
                            {
                              "name": "identifier-format",
                              "enabled": false,
                              "params": { "convention": "camelCase" }
                            }
                          ]
                        }
                        """.trimIndent()
                    parser.parse(json)
                },
                expectedRulesCount = 1,
            ),
            ConfigParserSuccessCase(
                name = "parse from file",
                execute = { parser ->
                    val tempFile = File.createTempFile("test-config", ".json")
                    tempFile.deleteOnExit()
                    tempFile.writeText(
                        """
                        {
                          "rules": [
                            {
                              "name": "println-no-expression",
                              "enabled": true
                            }
                          ]
                        }
                        """.trimIndent(),
                    )
                    parser.parse(tempFile)
                },
                expectedRulesCount = 1,
            ),
            ConfigParserSuccessCase(
                name = "parse empty rules list",
                execute = { parser ->
                    val json = """{ "rules": [] }"""
                    parser.parse(json)
                },
                expectedRulesCount = 0,
            ),
            ConfigParserSuccessCase(
                name = "parse config with unknown root properties ignored",
                execute = { parser ->
                    val json =
                        """
                        {
                          "extraProperty": "shouldBeIgnored",
                          "rules": [
                            {
                              "name": "println-no-expression",
                              "enabled": true
                            }
                          ]
                        }
                        """.trimIndent()
                    parser.parse(json)
                },
                expectedRulesCount = 1,
            ),
        )
}
