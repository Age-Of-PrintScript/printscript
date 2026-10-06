package linter.cases

import linter.LinterTestCase

internal object LinterSuccessCases {
    private val snakeCaseConfig =
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

    private val printlnOnlyConfig =
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

    private val disabledPrintlnConfig =
        """
        {
          "rules": [
            {
              "name": "println-no-expression",
              "enabled": false
            }
          ]
        }
        """.trimIndent()

    private val camelCaseConfig =
        """
        {
          "rules": [
            {
              "name": "identifier-format",
              "enabled": true,
              "params": { "convention": "camelCase" }
            }
          ]
        }
        """.trimIndent()

    fun cases(): List<LinterTestCase> =
        listOf(
            LinterTestCase(
                name = "default linter with valid camelCase identifier and literal println",
                source = "let myVar: string = \"hello\";\nprintln(myVar);",
                expectedWarningsCount = 0,
            ),
            LinterTestCase(
                name = "default linter flags PascalCase identifier with camelCase rule",
                source = "let MyVar: string = \"test\";",
                expectedWarningsCount = 1,
            ),
            LinterTestCase(
                name = "default linter flags expression inside println",
                source = "println(1 + 2);",
                expectedWarningsCount = 1,
            ),
            LinterTestCase(
                name = "default linter allows single letter lowercase identifier",
                source = "let x: number = 5;",
                expectedWarningsCount = 0,
            ),
            LinterTestCase(
                name = "custom snake_case convention flags camelCase",
                source = "let myVar: string = \"test\";",
                expectedWarningsCount = 1,
                configJson = snakeCaseConfig,
            ),
            LinterTestCase(
                name = "custom snake_case convention accepts valid lowercase identifier",
                source = "let myvar: string = \"test\";",
                expectedWarningsCount = 0,
                configJson = snakeCaseConfig,
            ),
            LinterTestCase(
                name = "custom println rule flags expression",
                source = "println(5 * 2);",
                expectedWarningsCount = 1,
                configJson = printlnOnlyConfig,
            ),
            LinterTestCase(
                name = "disabled println rule allows expression",
                source = "println(1 + 2);",
                expectedWarningsCount = 0,
                configJson = disabledPrintlnConfig,
            ),
            LinterTestCase(
                name = "custom camelCase rule flags invalid identifier",
                source = "let MyVar: string = \"test\";",
                expectedWarningsCount = 1,
                configJson = camelCaseConfig,
            ),
        )
}
