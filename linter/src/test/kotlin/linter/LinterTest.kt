package linter

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LinterTest {
    @Test
    fun testCreateDefaultLinter() {
        val linter = Linter.createDefault()
        val warnings = linter.analyse("let a_var: string = \"test\";")
        assertEquals(1, warnings.size)
    }

    @Test
    fun testFromConfigStream() {
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
        val linter = Linter.fromConfig(json.byteInputStream())
        val warningsCamel = linter.analyse("let myVar: string = \"test\";")
        assertEquals(1, warningsCamel.size)

        val warningsSnake = linter.analyse("let myvar: string = \"test\";")
        assertTrue(warningsSnake.isEmpty())
    }

    @Test
    fun testFromConfigFile() {
        val tempFile = File.createTempFile("linter-test-config", ".json")
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

        val linter = Linter.fromConfigFile(tempFile)
        val warnings = linter.analyse("println(1 + 2);")
        assertEquals(1, warnings.size)
    }

    @Test
    fun testFromJson() {
        val json =
            """
            {
              "rules": [
                {
                  "name": "println-no-expression",
                  "enabled": true
                }
              ]
            }
            """.trimIndent()
        val linter = Linter.fromJson(json)
        val warnings = linter.analyse("println(1 + 2);")
        assertEquals(1, warnings.size)
    }

    @Test
    fun testFromRules() {
        val rulesConfig = ConfigParser().parseDefault()
        val linter = Linter.fromRules(rulesConfig)
        val warnings = linter.analyse("let a_var: string = \"test\";")
        assertEquals(1, warnings.size)
    }

    @Test
    fun testLexerErrorReturnsWarning() {
        val linter = Linter.createDefault()
        val warnings = linter.analyse("@invalid")
        assertEquals(1, warnings.size)
    }

    @Test
    fun testParserErrorReturnsWarning() {
        val linter = Linter.createDefault()
        val warnings = linter.analyse("let = 5;")
        assertEquals(1, warnings.size)
    }
}
