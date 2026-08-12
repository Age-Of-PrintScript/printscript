package lexer

import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptSymbols
import domain.PrintScriptType
import domain.PrintScriptValue.StringLiteral
import domain.PrintScriptValue.NumberLiteral
import domain.Success
import lexer.states.Done
import lexer.states.Next
import lexer.states.StateResult
import tokens.ASSIGN
import tokens.COLON
import tokens.Call
import tokens.DataType
import tokens.LET
import tokens.Operator
import tokens.SEMICOLON
import tokens.TokenType

object Lexicon {
    val KEYWORDS: Map<String, TokenType> = mapOf(
        "let" to LET,
        "println" to Call(PrintScriptFunctions.PRINTLN),
        "number" to DataType(PrintScriptType.NUMBER),
        "string" to DataType(PrintScriptType.STRING)
    )
}

internal fun createSymbolTokenMap(): Map<String, TokenType> {
    val tokenMap = mutableMapOf<String, TokenType>()
    for (symbol in PrintScriptSymbols.entries) {
        when (symbol) {
            PrintScriptSymbols.SUM -> tokenMap[symbol.symbol] = Operator(PrintScriptOperator.SUM)
            PrintScriptSymbols.SUBTRACT -> tokenMap[symbol.symbol] = Operator(PrintScriptOperator.SUBTRACT)
            PrintScriptSymbols.MULTIPLY -> tokenMap[symbol.symbol] = Operator(PrintScriptOperator.MULTIPLY)
            PrintScriptSymbols.DIVIDE -> tokenMap[symbol.symbol] = Operator(PrintScriptOperator.DIVIDE)
            PrintScriptSymbols.COLON -> tokenMap[symbol.symbol] = COLON
            PrintScriptSymbols.SEMICOLON -> tokenMap[symbol.symbol] = SEMICOLON
            PrintScriptSymbols.ASSIGN -> tokenMap[symbol.symbol] = ASSIGN
            PrintScriptSymbols.OPEN_PARENTHESIS -> TODO()
            PrintScriptSymbols.CLOSE_PARENTHESIS -> TODO()
        }
    }
    return tokenMap.toMap()
}

internal fun createSymbolStateMap(): Map<String, StateResult> {
    val stateMap = mutableMapOf<String, StateResult>()
    for (symbol in PrintScriptSymbols.entries) {
        when (symbol) {
            PrintScriptSymbols.SUM -> stateMap[symbol.symbol] = Done
            PrintScriptSymbols.SUBTRACT -> stateMap[symbol.symbol] = Done
            PrintScriptSymbols.MULTIPLY -> stateMap[symbol.symbol] = Done
            PrintScriptSymbols.DIVIDE -> stateMap[symbol.symbol] = Done
            PrintScriptSymbols.COLON -> stateMap[symbol.symbol] = Done
            PrintScriptSymbols.SEMICOLON -> stateMap[symbol.symbol] = Done
            PrintScriptSymbols.ASSIGN -> stateMap[symbol.symbol] = Done
            PrintScriptSymbols.OPEN_PARENTHESIS -> stateMap[symbol.symbol] = Done
            PrintScriptSymbols.CLOSE_PARENTHESIS -> stateMap[symbol.symbol] = Done
        }
    }
    return stateMap.toMap()
}

internal fun concatNumbers(a: NumberLiteral, b: Char): NumberLiteral {
    val newValue = a.value.toDouble() * 10 + b.digitToInt()
    return NumberLiteral(newValue)
}

internal fun concatStrings(a: StringLiteral, b: Char): StringLiteral {
    val newValue = a.value + b.toString()
    return StringLiteral(newValue)
}
