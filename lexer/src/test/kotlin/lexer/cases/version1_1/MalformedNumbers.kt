package lexer.cases.version1_1

import lexer.FailureCase
import lexer.LexerError

object MalformedNumbers {
    fun cases() =
        listOf(
            FailureCase(
                "number with two decimal points in const",
                "const x: number = 5.5.5;",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "number with two decimal points in if condition",
                "if (5.5.5) { println(1); }",
                LexerError.INVALID_CHARACTER,
            ),
        )
}
