/**
 * # PRINTSCRIPT DATATYPES
 * These are the supported datatypes in printscript
 * last updated: 6/8/26
 */

enum class PrintScriptType{
    NUMBER,
    STRING
}
sealed interface PrintScriptValue{
    data class NumberLiteral(val value: Number): PrintScriptValue
    data class StringLiteral(val value: String): PrintScriptValue
}

/**
 * # PRINTSCRIPT OPERATIONS
 * * These are the supported operations in printscript
 * last updated: 6/8/26
 */

enum class PrintScriptOperator {
    SUM, SUBTRACT, MULTIPLY, DIVIDE
}
val termSeparators = listOf(PrintScriptOperator.SUM, PrintScriptOperator.SUBTRACT)
val factorSeparators = listOf(PrintScriptOperator.MULTIPLY, PrintScriptOperator.DIVIDE)


/**
 * # PRINTSCRIPT OPERATIONS
 * * This is the only built-in function in printscript
 * last updated: 6/8/26
 */

enum class PrintScriptFunctions{
    PRINTLN
}
