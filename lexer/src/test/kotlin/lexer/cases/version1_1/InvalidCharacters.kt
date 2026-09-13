package lexer.cases.version1_1

import lexer.FailureCase
import lexer.LexerError

object InvalidCharacters {
    fun cases() =
        listOf(
            FailureCase(
                "at symbol in const declaration",
                "const x: number = 5@;",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "euro symbol in const declaration",
                "const x: number = 5€;",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "hash symbol in boolean value",
                "let x: boolean = true#;",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "percent symbol in readInput expression",
                "let x: boolean = readInput(\"a\") % 2;",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "dollar symbol in readEnv",
                "readEnv(\"PATH\")$",
                LexerError.INVALID_CHARACTER,
            ),
            FailureCase(
                "unsupported ampersand in condition",
                "if (x & y) { println(1); }",
                LexerError.INVALID_CHARACTER,
            ),
        )
}
