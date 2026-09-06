package lexer.cases

import lexer.SuccessCase
import tokens.TokenTypeViejo

object SuccessfulEdgeCases {
    fun cases() =
        listOf(
            SuccessCase(
                "empty input returns empty token list",
                "",
                emptyList<TokenTypeViejo>(),
            ),
        )
}
