package lexer.cases

import lexer.SuccessCase
import tokens.TokenType

object SuccessfulEdgeCases {
    fun cases() =
        listOf(
            SuccessCase(
                "empty input returns empty token list",
                "",
                emptyList<TokenType>(),
            ),
        )
}
