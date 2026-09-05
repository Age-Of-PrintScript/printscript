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
                    parser.parse(json)
                },
                expectedException = IllegalArgumentException::class,
            ),
            ConfigParserFailureCase(
                name = "parse invalid json syntax throws SerializationException",
                execute = { parser ->
                    val json = """{ invalid json }"""
                    parser.parse(json)
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
                    parser.parse(json)
                },
                expectedException = IllegalArgumentException::class,
            ),
        )
}
