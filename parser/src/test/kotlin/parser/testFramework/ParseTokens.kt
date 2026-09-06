package parser.testFramework

import domain.Position
import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptType
import tokens.ASSIGNViejo
import tokens.CLOSED_PARENTHESISViejo
import tokens.COLONViejo
import tokens.CallViejo
import tokens.DataTypeViejo
import tokens.IdentifierViejo
import tokens.LETViejo
import tokens.LiteralViejo
import tokens.OPEN_PARENTHESISViejo
import tokens.OperatorViejo
import tokens.SEMICOLONViejo
import tokens.TokenTypeViejo
import tokens.TokenViejo

private val dummyPosition = Position(0, 0)

internal fun parseInputSection(lines: List<String>): List<TokenViejo> =
    lines.map { line ->
        TokenViejo(parseTokenType(line), dummyPosition, dummyPosition)
    }

private fun parseTokenType(line: String): TokenTypeViejo {
    val parts = line.split(":", limit = 2).map { it.trim() }
    val tag = parts[0]
    val value = parts.getOrNull(1)

    return when (tag) {
        "LET" -> LETViejo
        "COLON" -> COLONViejo
        "SEMICOLON" -> SEMICOLONViejo
        "ASSIGN" -> ASSIGNViejo
        "OPEN_PARENTHESIS" -> OPEN_PARENTHESISViejo
        "CLOSED_PARENTHESIS" -> CLOSED_PARENTHESISViejo
        "IDENTIFIER" -> IdentifierViejo(value!!)
        "TYPE" -> DataTypeViejo(PrintScriptType.valueOf(value!!))
        "LITERAL" -> LiteralViejo(value!!)
        "OPERATOR" -> OperatorViejo(PrintScriptOperator.valueOf(value!!))
        "CALL" -> CallViejo(PrintScriptFunctions.valueOf(value!!))
        else -> throw IllegalArgumentException("Tag de token desconocido: $tag en línea: $line")
    }
}
