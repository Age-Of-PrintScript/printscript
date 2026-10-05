package linter.cases

import linter.ConfigParserSuccessCase

internal object ConfigParserSuccessCases {
    fun cases(): List<ConfigParserSuccessCase> =
        listOf(
            ConfigParserSuccessCase(
                name = "resolve default config from resources",
                execute = { parser -> parser.resolve(null, "1.0") },
                expectedRulesCount = 2,
            ),
            ConfigParserSuccessCase(
                name = "resolve custom config",
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
                    parser.resolve(json, "1.0")
                },
                expectedRulesCount = 1,
            ),
            ConfigParserSuccessCase(
                name = "resolve config with disabled rules",
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
                    parser.resolve(json, "1.0")
                },
                expectedRulesCount = 1,
            ),
            ConfigParserSuccessCase(
                name = "resolve empty rules list",
                execute = { parser -> parser.resolve("""{ "rules": [] }""", "1.0") },
                expectedRulesCount = 0,
            ),
            ConfigParserSuccessCase(
                name = "resolve config with unknown root properties",
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
                    parser.resolve(json, "1.0")
                },
                expectedRulesCount = 1,
            ),
            ConfigParserSuccessCase(
                name = "resolve readInput rule in version 1.1",
                execute = { parser ->
                    val json =
                        """
                        {
                          "rules": [
                            {
                              "name": "readInput-no-expression",
                              "enabled": true
                            }
                          ]
                        }
                        """.trimIndent()
                    parser.resolve(json, "1.1")
                },
                expectedRulesCount = 1,
            ),
        )
}
