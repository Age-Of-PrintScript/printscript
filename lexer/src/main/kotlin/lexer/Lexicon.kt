package lexer

import domain.PrintScriptFunctions
import domain.PrintScriptOperator
import domain.PrintScriptReservedWords
import domain.PrintScriptSymbols
import domain.PrintScriptType
import domain.keywordRegistry
import lexer.states.FinalState
import lexer.states.State
import tokens.ASSIGNViejo
import tokens.CLOSED_PARENTHESISViejo
import tokens.COLONViejo
import tokens.CallViejo
import tokens.DataTypeViejo
import tokens.LETViejo
import tokens.OPEN_PARENTHESISViejo
import tokens.OperatorViejo
import tokens.SEMICOLONViejo
import tokens.TokenTypeViejo

internal fun createSymbolKeywordMap(): Map<String, TokenTypeViejo> {
    val keywordMap = mutableMapOf<String, TokenTypeViejo>()
    for (keyword in keywordRegistry) {
        when (keyword) {
            PrintScriptFunctions.PRINTLN ->
                keywordMap["println"] = CallViejo(PrintScriptFunctions.PRINTLN)

            PrintScriptReservedWords.LET -> keywordMap["let"] = LETViejo

            PrintScriptType.NUMBER ->
                keywordMap["number"] = DataTypeViejo(PrintScriptType.NUMBER)

            PrintScriptType.STRING ->
                keywordMap["string"] = DataTypeViejo(PrintScriptType.STRING)
        }
    }
    return keywordMap.toMap()
}

internal fun createSymbolTokenMap(): Map<Char, TokenTypeViejo> {
    val tokenMap = mutableMapOf<Char, TokenTypeViejo>()
    for (symbol in PrintScriptSymbols.entries) {
        when (symbol) {
            PrintScriptSymbols.SUM -> tokenMap[symbol.symbol] = OperatorViejo(PrintScriptOperator.SUM)
            PrintScriptSymbols.SUBTRACT -> tokenMap[symbol.symbol] = OperatorViejo(PrintScriptOperator.SUBTRACT)
            PrintScriptSymbols.MULTIPLY -> tokenMap[symbol.symbol] = OperatorViejo(PrintScriptOperator.MULTIPLY)
            PrintScriptSymbols.DIVIDE -> tokenMap[symbol.symbol] = OperatorViejo(PrintScriptOperator.DIVIDE)
            PrintScriptSymbols.COLON -> tokenMap[symbol.symbol] = COLONViejo
            PrintScriptSymbols.SEMICOLON -> tokenMap[symbol.symbol] = SEMICOLONViejo
            PrintScriptSymbols.ASSIGN -> tokenMap[symbol.symbol] = ASSIGNViejo
            PrintScriptSymbols.OPEN_PARENTHESIS -> tokenMap[symbol.symbol] = OPEN_PARENTHESISViejo
            PrintScriptSymbols.CLOSE_PARENTHESIS -> tokenMap[symbol.symbol] = CLOSED_PARENTHESISViejo
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
