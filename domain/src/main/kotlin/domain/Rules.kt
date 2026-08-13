package domain

sealed interface Keyword

enum class PrintScriptType:Keyword {
    NUMBER,
    STRING
}

sealed interface PrintScriptValue {
    data class NumberLiteral(val value: Number): PrintScriptValue
    data class StringLiteral(val value: String): PrintScriptValue
}

enum class PrintScriptOperator {
    SUM, SUBTRACT, MULTIPLY, DIVIDE, OPEN_PARENTHESIS, CLOSE_PARENTHESIS
}

val termSeparators = listOf(PrintScriptOperator.SUM, PrintScriptOperator.SUBTRACT)

val factorSeparators = listOf(PrintScriptOperator.MULTIPLY, PrintScriptOperator.DIVIDE)

enum class PrintScriptFunctions:Keyword {
    PRINTLN
}

enum class PrintScriptSymbols(val symbol: Char) {
    SUM('+'),
    SUBTRACT('-'),
    MULTIPLY('*'),
    DIVIDE('/'),
    COLON(':'),
    SEMICOLON(';'),
    ASSIGN('='),
    OPEN_PARENTHESIS('('),
    CLOSE_PARENTHESIS(')'),
}

enum class PrintScriptReservedWords: Keyword {
    LET
}

val keywordRegistry: List<Keyword> = PrintScriptFunctions.entries + PrintScriptReservedWords.entries + PrintScriptType.entries
