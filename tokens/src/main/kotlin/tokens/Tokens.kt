package tokens

import domain.Position

data class Token(
    val type: TokenType,
    val start: Position,
    val end: Position,
)

sealed interface TokenType
