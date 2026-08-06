/**
 * # PRINTSCRIPT DATATYPES
 * This are the supported datatypes in printscript
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
 * * This are the supported operations in printscript
 * last updated: 6/8/26
 */

enum class PrintScriptOperator {
    SUM, SUBTRACT, MULTIPLY, DIVIDE
}
