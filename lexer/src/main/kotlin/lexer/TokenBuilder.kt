package lexer

import domain.Either
import domain.Failure
import domain.Position
import domain.Success
import domain.getOrReturn
import tokens.Identifier
import tokens.Literal
import tokens.Token
import tokens.TokenType
import tokens.WHITESPACE

internal data class TokenBuilder(
    val type: TokenType? = null,
    val tokenMap: Map<Char, TokenType> = createSymbolTokenMap()
) {
    fun addChar(chr: Char): Either<LexerError, TokenBuilder> {
        if (type is Literal && !charIsQuote(chr)) {
            val newType = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
            return Success(copy(type = newType))
        }

        when {
            chr.isDigit() -> {
                val newType =
                    if (type == null) Literal(chr.toString())
                    else updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }

                return Success(copy(type = newType))
            }

            chr.isLetter() -> {
                val newType =
                    if (type == null) Identifier(chr.toString())
                    else updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }

                return Success(copy(type = newType))
            }

            chr == '.' -> {
                if (type is Literal) {
                    val newType = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    return Success(copy(type = newType))
                }
                return Failure(LexerError.INVALID_CHARACTER)
            }

            chr == '\'' || chr == '"' -> {
                val newType = when (type) {
                    is Literal -> updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    null -> Literal(chr.toString())
                    else -> return Failure(LexerError.INVALID_CHARACTER)
                }

                return Success(copy(type = newType))
            }

            chr.isWhitespace() -> return Success(copy(type = WHITESPACE))

            else -> {
                if (tokenMap.containsKey(chr))
                    return Success(copy(type = tokenMap.getValue(chr)))
                else return Failure(LexerError.INVALID_CHARACTER)
            }
        }
    }

    private fun updateTypeWithLiteral(type: TokenType?, chr: Char): Either<LexerError, TokenType> {
        return when (type) {
            is Identifier -> Success(Identifier(type.name + chr))
            is Literal ->  Success(Literal(type.value + chr))
            else -> Failure(LexerError.INVALID_CHARACTER_FOR_TOKEN_TYPE)
        }
    }

    fun build(): Either<LexerError, Token> {
        val finalType = resolveFinalType(type).getOrReturn { return Failure(it) }
        return Success(
            Token(
                finalType,
                Position(0, 0),
                Position(0,0)
            )
        )
    }

    private fun resolveFinalType(actualType: TokenType?): Either<LexerError, TokenType> {
        var finalType = actualType ?: return Failure(LexerError.UNDETERMINED_TOKEN_TYPE)

        if (finalType is Identifier) {
            val keywordMap = createSymbolKeywordMap()
            if (keywordMap.contains(finalType.name))
                finalType = keywordMap.getValue(finalType.name)
        }

        if (finalType is Literal) {
            val str = finalType.value
            //El type solo es asignado string type si arranca con comillas
            if (str.isNotEmpty() && charIsQuote(str.first())) {
                val last = str.last()
                if (!charIsQuote(last)) return Failure(LexerError.UNTERMINATED_STRING)
                else finalType = Literal(str.substring(1, str.length - 1))
            }
        }

        return Success(finalType)
    }
}
