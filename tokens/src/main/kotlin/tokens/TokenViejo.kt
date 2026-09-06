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

object LETViejo : TokenTypeViejo

object COLONViejo : TokenTypeViejo

object SEMICOLONViejo : TokenTypeViejo

object ASSIGNViejo : TokenTypeViejo

object WHITESPACEViejo : TokenTypeViejo

object OPEN_PARENTHESISViejo : TokenTypeViejo

object CLOSED_PARENTHESISViejo : TokenTypeViejo

data class OperatorViejo(
    val operator: PrintScriptOperator,
) : TokenTypeViejo

// Nombre de variable
data class IdentifierViejo(
    val name: String,
) : TokenTypeViejo

// valor de literal
data class LiteralViejo(
    val value: String,
) : TokenTypeViejo

// tipo de dato
data class DataTypeViejo(
    val type: PrintScriptType,
) : TokenTypeViejo

// llamado a una funcion
data class CallViejo(
    val type: PrintScriptFunctions,
) : TokenTypeViejo

typealias TokenList = List<TokenViejo>
