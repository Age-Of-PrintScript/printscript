package lexer.cases

import lexer.FailureCase
import lexer.LexerError

object MalformedNumbers {
    fun cases() =
        listOf(
            FailureCase(
                "number with two decimal points",
                "let x: number = 5.5.5;",
                LexerError.INVALID_CHARACTER,
            ),
        )
}
