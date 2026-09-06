package tokens

import domain.PSOperator
import domain.PSType
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

object CloseBraces : TokenType

object Whitespace : TokenType

object If : TokenType

object Else : TokenType

object Const : TokenType

data class Operator(
    val operator: PSOperator,
) : TokenType

data class Identifier(
    val name: String,
) : TokenType

data class Literal(
    val value: String,
    val type: PSType,
) : TokenType

data class DataType(
    val name: PSType,
) : TokenType

data class Call(
    val name: String,
) : TokenType
