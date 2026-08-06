data class Token(
    val type: TokenType,
    val start: Position,
    val end: Position
)


sealed interface TokenType {}

object LET: TokenType
object COLON: TokenType
object SEMICOLON: TokenType
object ASSIGN: TokenType
object NUMBER_TYPE: TokenType
object STRING_TYPE: TokenType

enum class Operator: TokenType {
    SUM, SUBSTRACT, MULTIPLY, DIVIDE
}

data class Identifier(val name: String): TokenType

data class NumberLiteral(val number: Number): TokenType

data class StringLiteral(val string: String): TokenType

