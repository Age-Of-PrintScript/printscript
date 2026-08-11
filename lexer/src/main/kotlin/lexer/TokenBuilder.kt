package lexer

import ASSIGN
import COLON
import Either
import Failure
import Identifier
import Literal
import Operator
import Position
import PrintScriptValue.*
import SEMICOLON
import Success
import Token
import TokenType

internal class TokenBuilder {
    private var type: TokenType? = null

    fun addChar(chr: Char): Either<LexerError, Unit> {
        when {
            chr.isDigit() -> {
                if (type == null) type = Literal(NumberLiteral(chr.code))
                else {
                    val newType = updateTypeWithNumber(type, chr)
                    when (newType) {
                        is Success -> type = newType.value
                        is Failure -> return Failure(newType.value)
                    }
                }
            }
            chr.isLetter() -> {
                if (type == null) type = Identifier(chr.toString())
                else {
                    val newType = updateTypeWithString(type, chr)
                    when (newType) {
                        is Success -> type = newType.value
                        is Failure -> return Failure(newType.value)
                    }
                }
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
            else -> return Failure(LexerError.INVALID_CHARACTER)
        }
        return Success(Unit)
    }

    private fun updateTypeWithNumber(type: TokenType?, chr: Char): Either<LexerError, TokenType> {
        return when (type) {
            is Identifier -> Success(Identifier(type.name + chr))
            is Literal -> {
                when (val newType = type.value) {
                    is NumberLiteral -> Success(Literal(newType.concatNumber(chr)))
                    is StringLiteral -> Success(Literal(newType.concatString(chr)))
                }
            }
            else -> Failure(LexerError.INVALID_CHARACTER_FOR_TOKEN_TYPE)
        }
    }

    private fun updateTypeWithString(type: TokenType?, chr: Char): Either<LexerError, TokenType> {
        return when (type) {
            is Identifier -> Success(Identifier(type.name + chr))
            is Literal -> {
                when (val newType = type.value) {
                    is NumberLiteral -> Failure(LexerError.INVALID_CHARACTER_FOR_TOKEN_TYPE)
                    is StringLiteral -> Success(Literal(newType.concatString(chr)))
                }
            }
            else -> Failure(LexerError.INVALID_CHARACTER_FOR_TOKEN_TYPE)
        }
    }

    fun build(): Either<LexerError, Token> {
        if (type == null) return Failure(LexerError.UNDETERMINED_TOKEN_TYPE)
        if (isStringLiteralAndDoesNotEnd(type)) return Failure(LexerError.UNTERMINATED_STRING)
        return Success(
            Token(
            type!!,
            Position(0,0),
            Position(0,0)
        ))
    }

    private fun isStringLiteralAndDoesNotEnd(type: TokenType?): Boolean {
        val value = (type as? Literal)?.value
        if (value is StringLiteral) {
            val str = value.value
            if (str[str.length - 1] != '"' || str[str.length - 1] != '\'') {
                return true
            }
        }
        return false
    }

    fun reset() {
        type = null
    }
}
