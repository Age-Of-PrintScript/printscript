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
            FailureCase(
                "open brace symbol in 1.0",
                "if (x) { println(1); }",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "close brace symbol in 1.0",
                "}",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "braces block in 1.0",
                "{ let x: number = 5; }",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "else block with braces in 1.0",
                "else { println(2); }",
                LexerError.INVALID_CHARACTER,
            ),
        )
}
