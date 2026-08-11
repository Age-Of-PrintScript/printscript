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
    data class NumberLiteral(val value: Number): PrintScriptValue {
        fun concatNumber(num: Number): NumberLiteral {
            val newValue = (value.toDouble() * 10 + num.toDouble()) as Number
            return NumberLiteral(newValue)
        }
        fun concatNumber(chr: Char): NumberLiteral {
            val num = chr.code
            return concatNumber(num)
        }
    }
    data class StringLiteral(val value: String): PrintScriptValue {
        fun concatString(chr: Char): StringLiteral {
            val newValue = value + chr.toString()
            return StringLiteral(newValue)
        }
    }
}

/**
 * # PRINTSCRIPT OPERATIONS
 * * These are the supported operations in printscript
 * last updated: 6/8/26
 */

enum class PrintScriptOperator {
    SUM, SUBTRACT, MULTIPLY, DIVIDE, OPEN_PARENTHESIS, CLOSE_PARENTHESIS
}


/**
 * # PRINTSCRIPT OPERATIONS
 * * This is the only built-in function in printscript
 * last updated: 6/8/26
 */

enum class PrintScriptFunctions{
    PRINTLN
}
