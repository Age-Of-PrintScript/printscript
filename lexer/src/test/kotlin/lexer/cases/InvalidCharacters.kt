package lexer.cases

import lexer.FailureCase
import lexer.LexerError

object InvalidCharacters {
    fun cases() =
        listOf(
            FailureCase(
                "at symbol as value",
                "let x: number = 5@;",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "euro symbol as value",
                "let x: number = 5€;",
                LexerError.INVALID_CHARACTER,
            ),
        )
}
