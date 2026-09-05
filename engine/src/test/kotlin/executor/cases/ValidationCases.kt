package executor.cases

object ValidationCases {
    data class Case(
        val name: String,
        val input: String,
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
        )
}
