import PrintScriptValue.*

internal class TokenBuilder {
    private var type: TokenType? = null

    fun addChar(chr: Char) {
        when {
            chr.isDigit() -> {
                if (type == null) type = Literal(NumberLiteral(chr.code))
                else type = updateTypeWithNumber(type, chr)
            }
            chr.isLetter() -> {
                if (type == null) type = Identifier(chr.toString())
                else type = updateTypeWithString(type, chr)
            }
            chr == '\'' -> type = Literal(StringLiteral(""))
            chr == '"' -> type = Literal(StringLiteral(""))
            chr == ':' -> type = COLON
            chr == ';' -> type = SEMICOLON
            chr == '=' -> type = ASSIGN
            chr == '+' -> type = Operator(PrintScriptOperator.SUM)
            chr == '-' -> type = Operator(PrintScriptOperator.SUBTRACT)
            chr == '*' -> type = Operator(PrintScriptOperator.MULTIPLY)
            chr == '/' -> type = Operator(PrintScriptOperator.DIVIDE)
            chr == '(' -> type = Operator(PrintScriptOperator.OPEN_PARENTHESIS)
            chr == ')' -> type = Operator(PrintScriptOperator.CLOSE_PARENTHESIS)
            else -> TODO()
        }
    }

    private fun updateTypeWithNumber(type: TokenType?, chr: Char): TokenType {
        return when (type) {
            is Identifier -> Identifier(type.name + chr)
            is Literal -> {
                when (val newType = type.value) {
                    is NumberLiteral -> Literal(newType.concatNumber(chr))
                    is StringLiteral -> Literal(newType.concatString(chr))
                }
            }
            else -> TODO()
        }
    }

    private fun updateTypeWithString(type: TokenType?, chr: Char): TokenType {
        return when (type) {
            is Identifier -> Identifier(type.name + chr)
            is Literal -> {
                when (val newType = type.value) {
                    is NumberLiteral -> TODO()
                    is StringLiteral -> Literal(newType.concatString(chr))
                }
            }
            else -> TODO()
        }
    }

    fun build(): Token {
        TODO()
    }
    fun reset() {}
}