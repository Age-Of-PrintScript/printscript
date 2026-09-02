package linter.cases

import linter.Linter
import linter.LinterTestCase

internal object LinterErrorCases {
    fun cases(): List<LinterTestCase> =
        listOf(
            LinterTestCase(
                name = "lexer error returns warning",
                linterProvider = { Linter.createDefault() },
                source = "@invalid",
                expectedWarningsCount = 1,
            ),
            LinterTestCase(
                name = "parser syntax error returns warning",
                linterProvider = { Linter.createDefault() },
                source = "let = 5;",
                expectedWarningsCount = 1,
            ),
            LinterTestCase(
                name = "empty source code returns no warnings",
                linterProvider = { Linter.createDefault() },
                source = "",
                expectedWarningsCount = 0,
            ),
            LinterTestCase(
                name = "whitespace only source code returns no warnings",
                linterProvider = { Linter.createDefault() },
                source = "   \n\t  ",
                expectedWarningsCount = 0,
            ),
        )
}
