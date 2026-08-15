package lexer

import domain.Either
import domain.Failure
import domain.Position
import domain.PrintScriptValue.NumberLiteral
import domain.PrintScriptValue.StringLiteral
import domain.Success
import tokens.Identifier
import tokens.Literal
import tokens.Token
import tokens.TokenType
import tokens.WHITESPACE

internal class TokenBuilder {
    private var type: TokenType? = null
    private val tokenMap = createSymbolTokenMap()

    fun addChar(chr: Char): Either<LexerError, Unit> {
        if(isStringType(type) && charIsNotQuote(chr)){
            return handleStringLiteral(type, chr)
        }
        when {
            chr.isDigit() -> {
                if (type == null) type = Literal(NumberLiteral(chr.digitToInt()))
                else {
                    when (val newType = updateTypeWithNumber(type, chr)) {
                        is Success -> type = newType.value
                        is Failure -> return Failure(newType.value)
                    }
                }
            }
            chr.isLetter() -> {
                if (type == null) type = Identifier(chr.toString())
                else return handleStringLiteral(type, chr)
            }
            chr == '.' -> {
                if (type == null) return Failure(LexerError.INVALID_CHARACTER)
                updateTypeWithNumber(type, chr)
            }
            chr == '\'' -> type = type ?: Literal(StringLiteral(""))
            chr == '"' -> type = type ?: Literal(StringLiteral(""))
            chr.isWhitespace() -> type = WHITESPACE
            else -> {
                if (tokenMap.containsKey(chr)) type = tokenMap.getValue(chr)
                else return Failure(LexerError.INVALID_CHARACTER)
            }
        }
        return Success(Unit)
    }

    private fun handleStringLiteral(type: TokenType?, chr: Char): Either<LexerError, Unit> {
        when (val result = updateTypeWithString(type, chr)) {
            is Failure -> return Failure(result.value)
            is Success -> {
                this.type = result.value
                return Success(Unit)
            }
        }
    }

    private fun updateTypeWithNumber(type: TokenType?, chr: Char): Either<LexerError, TokenType> {
        return when (type) {
            is Identifier -> Success(Identifier(type.name + chr))
            is Literal -> {
                when (val newType = type.value) {
                    is NumberLiteral -> Success(Literal(concatNumbers(newType, chr)))
                    is StringLiteral -> Success(Literal(concatStrings(newType, chr)))
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
                    is StringLiteral -> Success(Literal(concatStrings(newType, chr)))
                }
            }
            else -> Failure(LexerError.INVALID_CHARACTER_FOR_TOKEN_TYPE)
        }
    }

    fun build(): Either<LexerError, Token> {
        var finishedType = type ?: return Failure(LexerError.UNDETERMINED_TOKEN_TYPE)

        if (finishedType is Identifier) {
            val keywordMap = createSymbolKeywordMap()
            if (keywordMap.contains(finishedType.name))
                finishedType = keywordMap.getValue(finishedType.name)
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
