package tokens

import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType

data class TokenViejo(
    val type: TokenTypeViejo,
    val start: Position,
    val end: Position,
)

sealed interface TokenTypeViejo

object LET : TokenTypeViejo

object COLON : TokenTypeViejo

object SEMICOLON : TokenTypeViejo

object ASSIGN : TokenTypeViejo

object WHITESPACE : TokenTypeViejo

object OPEN_PARENTHESIS : TokenTypeViejo

object CLOSED_PARENTHESIS : TokenTypeViejo

data class Operator(
    val operator: PrintScriptOperator,
) : TokenTypeViejo

// Nombre de variable
data class Identifier(
    val name: String,
) : TokenTypeViejo

// valor de literal
data class Literal(
    val value: String,
) : TokenTypeViejo

// tipo de dato
data class DataTypeViejo(
    val type: PrintScriptType,
) : TokenTypeViejo

// llamado a una funcion
data class Call(
    val type: PrintScriptFunctions,
) : TokenTypeViejo

typealias TokenList = List<TokenViejo>
