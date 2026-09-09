package lexer

import domain.Either
import domain.Failure
import domain.NumType
import domain.Position
import domain.StrType
import domain.Success
import domain.getOrReturn
import tokens.Identifier
import tokens.Literal
import tokens.Token
import tokens.TokenType
import tokens.Whitespace

internal data class TokenBuilder(
    val lexicon: Lexicon,
    val type: TokenType? = null,
) {
    fun addChar(chr: Char): Either<LexerError, TokenBuilder> {
        if (type is Literal && !charIsQuote(chr)) {
            val newType = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
            return Success(copy(type = newType))
        }

        when {
            chr.isDigit() -> {
                val newType =
                    if (type == null) {
                        Literal(chr.toString(), NumType)
                    } else {
                        updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    }

                return Success(copy(type = newType))
            }

            chr.isLetter() -> {
                val newType =
                    if (type == null) {
                        Identifier(chr.toString())
                    } else {
                        updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    }

                return Success(copy(type = newType))
            }

            chr == '\'' || chr == '"' -> {
                val newType =
                    when (type) {
                        is Literal -> updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                        null -> Literal(chr.toString(), StrType)
                        else -> return Failure(LexerError.INVALID_CHARACTER)
                    }

                return Success(copy(type = newType))
            }

            chr == '.' -> return Failure(LexerError.INVALID_CHARACTER)

            chr.isWhitespace() -> return Success(copy(type = Whitespace))

            else -> {
                return if (lexicon.symbols.containsKey(chr)) {
                    Success(copy(type = lexicon.symbols.getValue(chr)))
                } else {
                    Failure(LexerError.INVALID_CHARACTER)
                }
            }
        }
    }

    private fun updateTypeWithLiteral(
        type: TokenType?,
        chr: Char,
    ): Either<LexerError, TokenType> =
        when (type) {
            is Identifier -> Success(Identifier(type.name + chr))
            is Literal -> Success(Literal(type.value + chr, type.type))
            is Whitespace -> Success(Whitespace)
            else -> Failure(LexerError.INVALID_CHARACTER_FOR_TOKEN_TYPE)
        }

    fun build(): Either<LexerError, Token> {
        val finalType = resolveFinalType(type).getOrReturn { return Failure(it) }
        return Success(
            Token(
                finalType,
                Position(0, 0),
                Position(0, 0),
            ),
        )
    }

    private fun resolveFinalType(actualType: TokenType?): Either<LexerError, TokenType> {
        var finalType = actualType ?: return Failure(LexerError.UNDETERMINED_TOKEN_TYPE)

        if (finalType is Identifier) {
            val keywordMap = lexicon.keywords
            if (keywordMap.contains(finalType.name)) {
                finalType = keywordMap.getValue(finalType.name)
            }
        }

        if (finalType is Literal) {
            val str = finalType.value
            // El type solo es asignado string type si arranca con comillas
            if (str.isNotEmpty() && charIsQuote(str.first())) {
                val last = str.last()
                if (!charIsQuote(last)) {
                    return Failure(LexerError.UNTERMINATED_STRING)
                } else {
                    finalType = Literal(str.substring(1, str.length - 1), StrType)
                }
            }
        }

        return Success(finalType)
    }
}
