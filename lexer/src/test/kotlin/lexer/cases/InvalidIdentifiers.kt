package lexer.cases

import lexer.FailureCase
import lexer.LexerError

object InvalidIdentifiers {
    fun cases() = listOf(
        FailureCase(
            "identifier starting with digit",
            "let 123x: number = 5;",
            LexerError.INVALID_CHARACTER
        )
    )
}