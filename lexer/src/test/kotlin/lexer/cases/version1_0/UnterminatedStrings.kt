package lexer.cases.version1_0

import lexer.FailureCase
import lexer.LexerError

object UnterminatedStrings {
    fun cases() =
        listOf(
            FailureCase(
                "unterminated string with content after opening quote",
                "let x: string = \"hola;",
                LexerError.UNTERMINATED_STRING,
            ),
            FailureCase(
                "unterminated multiline string",
                "let x: string = \"hola\n mundo;",
                LexerError.UNTERMINATED_STRING,
            ),
        )
}
