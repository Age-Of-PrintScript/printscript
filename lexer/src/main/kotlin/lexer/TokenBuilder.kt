package lexer

import domain.Either
import domain.Failure
import domain.Position
import domain.PrintScriptOperator
import domain.PrintScriptValue
import domain.Success
import tokens.ASSIGN
import tokens.COLON
import tokens.Identifier
import tokens.Literal
import tokens.Operator
import tokens.SEMICOLON
import tokens.Token
import tokens.TokenType

internal class TokenBuilder {
    private var type: TokenType? = null

    fun addChar(chr: Char): Either<LexerError, Unit> {
        when {
            chr.isDigit() -> {
                if (type == null) type = Literal(PrintScriptValue.NumberLiteral(chr.digitToInt()))
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
            chr == '\'' -> type = type ?: Literal(PrintScriptValue.StringLiteral(""))
            chr == '"' -> type = type ?: Literal(PrintScriptValue.StringLiteral(""))
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
                    is PrintScriptValue.NumberLiteral -> Success(Literal(concatNumbers(newType, chr)))
                    is PrintScriptValue.StringLiteral -> Success(Literal(concatStrings(newType, chr)))
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
                    is PrintScriptValue.NumberLiteral -> Failure(LexerError.INVALID_CHARACTER_FOR_TOKEN_TYPE)
                    is PrintScriptValue.StringLiteral -> Success(Literal(concatStrings(newType, chr)))
                }
            }
            else -> Failure(LexerError.INVALID_CHARACTER_FOR_TOKEN_TYPE)
        }
    }

    fun build(): Either<LexerError, Token> {
        var finishedType = type ?: return Failure(LexerError.UNDETERMINED_TOKEN_TYPE)

        if (finishedType is Identifier) {
            if (Lexicon.KEYWORDS.contains(finishedType.name)) {
                finishedType = Lexicon.KEYWORDS[finishedType.name]!!
            }
        }
        return Success(
            Token(
                finishedType,
                Position(0, 0),
                Position(0,0)
            )
        )
    }

    fun reset() {
        type = null
    }
}
