package linter.cases

import linter.ConfigParser
import linter.Linter
import linter.LinterTestCase
import java.io.File

internal object LinterSuccessCases {
    fun cases(): List<LinterTestCase> =
        listOf(
            LinterTestCase(
                name = "default linter with valid camelCase identifier and literal println",
                linterProvider = { Linter.createDefault() },
                source = "let myVar: string = \"hello\";\nprintln(myVar);",
                expectedWarningsCount = 0,
            ),
            LinterTestCase(
                name = "default linter flags PascalCase identifier with camelCase rule",
                linterProvider = { Linter.createDefault() },
                source = "let MyVar: string = \"test\";",
                expectedWarningsCount = 1,
            ),
            LinterTestCase(
                name = "default linter flags expression inside println",
                linterProvider = { Linter.createDefault() },
                source = "println(1 + 2);",
                expectedWarningsCount = 1,
            ),
            LinterTestCase(
                name = "default linter allows single letter lowercase identifier",
                linterProvider = { Linter.createDefault() },
                source = "let x: number = 5;",
                expectedWarningsCount = 0,
            ),
            LinterTestCase(
                name = "fromConfig stream with snake_case convention flags camelCase",
                linterProvider = {
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
                    Linter.fromConfig(json.byteInputStream())
                },
                source = "let myVar: string = \"test\";",
                expectedWarningsCount = 1,
            ),
            LinterTestCase(
                name = "fromConfig stream with snake_case convention accepts valid lowercase identifier",
                linterProvider = {
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
                    Linter.fromConfig(json.byteInputStream())
                },
                source = "let myvar: string = \"test\";",
                expectedWarningsCount = 0,
            ),
            LinterTestCase(
                name = "fromConfigFile with println rule flags expression",
                linterProvider = {
                    val tempFile = File.createTempFile("linter-cfg", ".json")
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
                    Linter.fromConfigFile(tempFile)
                },
                source = "println(5 * 2);",
                expectedWarningsCount = 1,
            ),
            LinterTestCase(
                name = "fromJson with disabled println rule allows expression",
                linterProvider = {
                    val json =
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
                    Linter.fromJson(json)
                },
                source = "println(1 + 2);",
                expectedWarningsCount = 0,
            ),
            LinterTestCase(
                name = "fromRules with programmatically parsed config",
                linterProvider = {
                    val rulesConfig = ConfigParser().parseDefault()
                    Linter.fromRules(rulesConfig)
                },
                source = "let MyVar: string = \"test\";",
                expectedWarningsCount = 1,
            ),
        )
}
