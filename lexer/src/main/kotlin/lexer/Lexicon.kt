package lexer

import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptReservedWords
import domain.PrintScriptSymbols
import domain.PrintScriptType
import domain.keywordRegistry
import lexer.states.FinalState
import lexer.states.State
import tokens.ASSIGN
import tokens.COLON
import tokens.Call
import tokens.DataType
import tokens.LET
import tokens.Operator
import tokens.SEMICOLON
import tokens.TokenType

internal fun createSymbolKeywordMap(): Map<String, TokenType> {
    val keywordMap = mutableMapOf<String, TokenType>()
    for (keyword in keywordRegistry) {
        when (keyword) {
            PrintScriptFunctions.PRINTLN ->
                keywordMap["println"] = Call(PrintScriptFunctions.PRINTLN)
            PrintScriptReservedWords.LET -> keywordMap["let"] = LET
            PrintScriptType.NUMBER ->
                keywordMap["number"] = DataType(PrintScriptType.NUMBER)
            PrintScriptType.STRING ->
                keywordMap["string"] = DataType(PrintScriptType.STRING)
        }
    }
    return keywordMap.toMap()
}

internal fun createSymbolTokenMap(): Map<Char, TokenType> {
    val tokenMap = mutableMapOf<Char, TokenType>()
    for (symbol in PrintScriptSymbols.entries) {
        when (symbol) {
            PrintScriptSymbols.SUM -> tokenMap[symbol.symbol] = Operator(PrintScriptOperator.SUM)
            PrintScriptSymbols.SUBTRACT -> tokenMap[symbol.symbol] = Operator(PrintScriptOperator.SUBTRACT)
            PrintScriptSymbols.MULTIPLY -> tokenMap[symbol.symbol] = Operator(PrintScriptOperator.MULTIPLY)
            PrintScriptSymbols.DIVIDE -> tokenMap[symbol.symbol] = Operator(PrintScriptOperator.DIVIDE)
            PrintScriptSymbols.COLON -> tokenMap[symbol.symbol] = COLON
            PrintScriptSymbols.SEMICOLON -> tokenMap[symbol.symbol] = SEMICOLON
            PrintScriptSymbols.ASSIGN -> tokenMap[symbol.symbol] = ASSIGN
            //TODO Esperando la pr de nacho
            PrintScriptSymbols.OPEN_PARENTHESIS -> continue
            PrintScriptSymbols.CLOSE_PARENTHESIS -> continue
        }
    }
    return tokenMap.toMap()
}

internal fun createSymbolStateMap(): Map<Char, State> {
    val stateMap = mutableMapOf<Char, State>()
    for (symbol in PrintScriptSymbols.entries) {
        when (symbol) {
            PrintScriptSymbols.SUM -> stateMap[symbol.symbol] = FinalState()
            PrintScriptSymbols.SUBTRACT -> stateMap[symbol.symbol] = FinalState()
            PrintScriptSymbols.MULTIPLY -> stateMap[symbol.symbol] = FinalState()
            PrintScriptSymbols.DIVIDE -> stateMap[symbol.symbol] = FinalState()
            PrintScriptSymbols.COLON -> stateMap[symbol.symbol] = FinalState()
            PrintScriptSymbols.SEMICOLON -> stateMap[symbol.symbol] = FinalState()
            PrintScriptSymbols.ASSIGN -> stateMap[symbol.symbol] = FinalState()
            PrintScriptSymbols.OPEN_PARENTHESIS -> stateMap[symbol.symbol] = FinalState()
            PrintScriptSymbols.CLOSE_PARENTHESIS -> stateMap[symbol.symbol] = FinalState()
        }
    }
    return stateMap.toMap()
}
