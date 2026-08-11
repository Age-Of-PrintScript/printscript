import tokens.Call
import tokens.DataType
import tokens.LET
import tokens.TokenType

object Lexicon {
    val KEYWORDS: Map<String, TokenType> = mapOf(
        "let" to LET,
        "println" to Call(PrintScriptFunctions.PRINTLN),
        "number" to DataType(PrintScriptType.NUMBER),
        "string" to DataType(PrintScriptType.STRING)
    )
}