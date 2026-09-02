package linter

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConfigParserTest {
    private val parser = ConfigParser()

    @Test
    fun testParseDefaultConfig() {
        val config = parser.parseDefault()
        assertEquals(2, config.rules.size)
    }

    @Test
    fun testParseOrDefaultWithNull() {
        val config = parser.parseOrDefault(null)
        assertEquals(2, config.rules.size)
    }

    @Test
    fun testParseOrDefaultWithCustomStream() {
        val customJson =
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
        val stream = customJson.byteInputStream()
        val config = parser.parseOrDefault(stream)
        assertEquals(1, config.rules.size)
    }

    @Test
    fun testParseFromJsonString() {
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
        val config = parser.parse(json)
        assertEquals(1, config.rules.size)
    }

    @Test
    fun testParseFromFile() {
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

        val config = parser.parse(tempFile)
        assertEquals(1, config.rules.size)
    }

    @Test
    fun testUnknownRuleThrowsException() {
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

        assertFailsWith<IllegalArgumentException> {
            parser.parse(json)
        }
    }
}
