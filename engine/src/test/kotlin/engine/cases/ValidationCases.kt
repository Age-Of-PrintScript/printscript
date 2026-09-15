package engine.cases

object ValidationCases {
    data class Case(
        val name: String,
        val input: String,
        val version: String? = null,
    )

    fun successfulCases() =
        listOf(
            Case(
                "valid variable declaration",
                "let x: number = 5;",
            ),
            Case(
                "valid print call",
                "println(\"hello\");",
            ),
            // Version 1.1 Cases
            Case(
                "v1.1 valid const declaration",
                "const x: number = 10;",
                version = "1.1",
            ),
            Case(
                "v1.1 valid boolean declaration and if statement",
                "let active: boolean = true;\nif (active) {\nprintln(\"Active\");\n} else {\nprintln(\"Inactive\");\n}",
                version = "1.1",
            ),
            Case(
                "v1.1 valid readInput and readEnv",
                "let input: string = readInput(\"Name: \");\nlet env: string = readEnv(\"ENV_VAR\");",
                version = "1.1",
            ),
        )

    fun failedCases() =
        listOf(
            Case(
                "lexer error during validation",
                "let x: number = 5@;",
            ),
            Case(
                "parser error during validation",
                "let x: = 5;",
            ),
            // Version 1.1 Cases
            Case(
                "v1.1 validation fails on unclosed braces in if block",
                "if (true) {\nprintln(1);",
                version = "1.1",
            ),
            Case(
                "v1.1 validation fails on else without if",
                "else {\nprintln(1);\n}",
                version = "1.1",
            ),
            Case(
                "v1.1 validation fails on const without initialization",
                "const x: number;",
                version = "1.1",
            ),
        )
}
