package lexer

import domain.PrintScriptFunctions
import domain.PrintScriptType
import domain.PrintScriptValue.StringLiteral
import domain.PrintScriptValue.NumberLiteral
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

fun concatNumbers(a: NumberLiteral, b: Char): NumberLiteral {
    val newValue = a.value.toDouble() * 10 + b.digitToInt()
    return NumberLiteral(newValue)
}

fun concatStrings(a: StringLiteral, b: Char): StringLiteral {
    val newValue = a.value + b.toString()
    return StringLiteral(newValue)
}
