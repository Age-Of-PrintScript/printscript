package lexer

import domain.PrintScriptValue.StringLiteral
import domain.PrintScriptValue.NumberLiteral


//TODO MEJORAR PARA QUE ACEPTE "."
internal fun concatNumbers(a: NumberLiteral, b: Char): NumberLiteral {
    val newValue = a.value.toDouble() * 10 + b.digitToInt()
    return NumberLiteral(newValue)
}

internal fun concatStrings(a: StringLiteral, b: Char): StringLiteral {
    val newValue = a.value + b.toString()
    return StringLiteral(newValue)
}
