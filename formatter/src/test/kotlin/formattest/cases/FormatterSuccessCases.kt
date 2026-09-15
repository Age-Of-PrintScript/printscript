package formattest.cases

object FormatterSuccessCases {
    fun cases(): List<FormatterSuccessCase> =
        listOf(
            // ----------------- v1.0 Cases -----------------
            FormatterSuccessCase(
                name = "v1.0 - declaration with spaces around equals and after colon",
                version = "1.0",
                source = "let x:number=5;",
                configJson =
                    """
                    {
                        "enforce-spacing-around-equals": true,
                        "enforce-spacing-after-colon-in-declaration": true
                    }
                    """.trimIndent(),
                expectedOutput = "let x: number = 5;\n",
            ),
            FormatterSuccessCase(
                name = "v1.0 - no spacing around equals",
                version = "1.0",
                source = "let x : number = 5; x = 10;",
                configJson =
                    """
                    {
                        "enforce-no-spacing-around-equals": true,
                        "enforce-spacing-after-colon-in-declaration": true
                    }
                    """.trimIndent(),
                expectedOutput = "let x: number=5;\nx=10;\n",
            ),
            FormatterSuccessCase(
                name = "v1.0 - spacing before and after colon in declaration",
                version = "1.0",
                source = "let name:string=\"John\";",
                configJson =
                    """
                    {
                        "enforce-spacing-before-colon-in-declaration": true,
                        "enforce-spacing-after-colon-in-declaration": true,
                        "enforce-spacing-around-equals": true
                    }
                    """.trimIndent(),
                expectedOutput = "let name : string = \"John\";\n",
            ),
            FormatterSuccessCase(
                name = "v1.0 - line breaks after println and operations spacing",
                version = "1.0",
                source = "let result: number = 1+2*3; println(result); let y: number = 10;",
                configJson =
                    """
                    {
                        "mandatory-space-surrounding-operations": true,
                        "enforce-spacing-around-equals": true,
                        "enforce-spacing-after-colon-in-declaration": true,
                        "line-breaks-after-println": 1
                    }
                    """.trimIndent(),
                expectedOutput = "let result: number = 1 + 2 * 3;\nprintln(result);\n\nlet y: number = 10;\n",
            ),
            FormatterSuccessCase(
                name = "v1.0 - mandatory single space separation",
                version = "1.0",
                source = "let name:string=\"John\"; println(name);",
                configJson =
                    """
                    {
                        "mandatory-single-space-separation": true
                    }
                    """.trimIndent(),
                expectedOutput = "let name : string = \"John\";\nprintln ( name );\n",
            ),
            // ----------------- v1.1 Cases -----------------
            FormatterSuccessCase(
                name = "v1.1 - if statement same line brace with indentation",
                version = "1.1",
                source = "if (true) { println(\"hello\"); }",
                configJson =
                    """
                    {
                        "if-brace-same-line": true,
                        "indent-inside-if": 4
                    }
                    """.trimIndent(),
                expectedOutput = "if (true) {\n    println(\"hello\");\n}\n",
            ),
            FormatterSuccessCase(
                name = "v1.1 - if else below line brace with indentation",
                version = "1.1",
                source = "if (x) { println(\"yes\"); } else { println(\"no\"); }",
                configJson =
                    """
                    {
                        "if-brace-below-line": true,
                        "indent-inside-if": 2
                    }
                    """.trimIndent(),
                expectedOutput = "if (x)\n{\n  println(\"yes\");\n} else\n{\n  println(\"no\");\n}\n",
            ),
            FormatterSuccessCase(
                name = "v1.1 - nested if conditionals",
                version = "1.1",
                source = "if (a) { if (b) { println(\"nested\"); } }",
                configJson =
                    """
                    {
                        "if-brace-same-line": true,
                        "indent-inside-if": 2
                    }
                    """.trimIndent(),
                expectedOutput = "if (a) {\n  if (b) {\n    println(\"nested\");\n  }\n}\n",
            ),
        )
}
