package tokens

import domain.Position

data class Token(
    val type: TokenType,
    val start: Position,
    val end: Position,
)

sealed interface TokenType

object Let : TokenType

object Colon : TokenType

object Semicolon : TokenType

object Assign : TokenType

object OpenParen : TokenType

object CloseParen : TokenType

object OpenBraces : TokenType

object ClosedBraces : TokenType

object Whitespace : TokenType
