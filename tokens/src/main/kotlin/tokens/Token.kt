package tokens

import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import domain.PrintScriptValue


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

object OpenParenthesis: TokenType
object ClosedParenthesis: TokenType

data class Operator(val operator: PrintScriptOperator): TokenType

//Nombre de variable
data class Identifier(val name: String): TokenType

//valor de literal
data class Literal(val value: PrintScriptValue): TokenType

//tipo de dato
data class DataType(val type: PrintScriptType): TokenType

//llamado a una funcion
data class Call(val type: PrintScriptFunctions): TokenType


typealias TokenList = List<Token>


