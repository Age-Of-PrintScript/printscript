package linter.cases

import kotlinx.serialization.SerializationException
import linter.ConfigParserFailureCase

internal object ConfigParserFailureCases {
    fun cases(): List<ConfigParserFailureCase> =
        listOf(
            ConfigParserFailureCase(
                name = "parse unknown rule throws IllegalArgumentException",
                execute = { parser ->
                    val json =
                        """
                        {
                          "rules": [
                            {
                              "name": "non-existent-rule",
                              "enabled": true
                            }
                          ]
                        }
                        """.trimIndent()
                    parser.resolve(json, "1.0")
                },
                expectedException = IllegalArgumentException::class,
            ),
            ConfigParserFailureCase(
                name = "parse invalid json syntax throws SerializationException",
                execute = { parser ->
                    val json = """{ invalid json }"""
                    parser.resolve(json, "1.0")
                },
                expectedException = SerializationException::class,
            ),
            ConfigParserFailureCase(
                name = "parse missing convention in identifier-format throws IllegalArgumentException",
                execute = { parser ->
                    val json =
                        """
                        {
                          "rules": [
                            {
                              "name": "identifier-format",
                              "enabled": true,
                              "params": {}
                            }
                          ]
                        }
                        """.trimIndent()
                    parser.resolve(json, "1.0")
                },
                expectedException = IllegalArgumentException::class,
            ),
            ConfigParserFailureCase(
                name = "parse readInput-no-expression in version 1.0 throws IllegalArgumentException",
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
                    parser.resolve(json, "1.0")
                },
                expectedException = IllegalArgumentException::class,
            ),
        )
}
