package lexer

import domain.Either
import domain.Failure
import domain.Position
import domain.Success
import domain.getOrReturn
import tokens.IdentifierViejo
import tokens.LiteralViejo
import tokens.TokenTypeViejo
import tokens.TokenViejo
import tokens.WHITESPACEViejo

internal data class TokenBuilder(
    val type: TokenTypeViejo? = null,
    val tokenMap: Map<Char, TokenTypeViejo> = createSymbolTokenMap(),
) {
    fun addChar(chr: Char): Either<LexerError, TokenBuilder> {
        if (type is LiteralViejo && !charIsQuote(chr)) {
            val newType = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
            return Success(copy(type = newType))
        }

        when {
            chr.isDigit() -> {
                val newType =
                    if (type == null) {
                        LiteralViejo(chr.toString())
                    } else {
                        updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    }

                return Success(copy(type = newType))
            }

            chr.isLetter() -> {
                val newType =
                    if (type == null) {
                        IdentifierViejo(chr.toString())
                    } else {
                        updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    }

                return Success(copy(type = newType))
            }

            chr == '\'' || chr == '"' -> {
                val newType =
                    when (type) {
                        is LiteralViejo -> updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                        null -> LiteralViejo(chr.toString())
                        else -> return Failure(LexerError.INVALID_CHARACTER)
                    }

                return Success(copy(type = newType))
            }

            chr == '.' -> return Failure(LexerError.INVALID_CHARACTER)

            chr.isWhitespace() -> return Success(copy(type = WHITESPACEViejo))

            else -> {
                return if (tokenMap.containsKey(chr)) {
                    Success(copy(type = tokenMap.getValue(chr)))
                } else {
                    Failure(LexerError.INVALID_CHARACTER)
                }
            }
        }
    }

    private fun updateTypeWithLiteral(
        type: TokenTypeViejo?,
        chr: Char,
    ): Either<LexerError, TokenTypeViejo> =
        when (type) {
            is IdentifierViejo -> Success(IdentifierViejo(type.name + chr))
            is LiteralViejo -> Success(LiteralViejo(type.value + chr))
            else -> Failure(LexerError.INVALID_CHARACTER_FOR_TOKEN_TYPE)
        }

    fun build(): Either<LexerError, TokenViejo> {
        val finalType = resolveFinalType(type).getOrReturn { return Failure(it) }
        return Success(
            TokenViejo(
                finalType,
                Position(0, 0),
                Position(0, 0),
            ),
        )
    }

    private fun resolveFinalType(actualType: TokenTypeViejo?): Either<LexerError, TokenTypeViejo> {
        var finalType = actualType ?: return Failure(LexerError.UNDETERMINED_TOKEN_TYPE)

        if (finalType is IdentifierViejo) {
            val keywordMap = createSymbolKeywordMap()
            if (keywordMap.contains(finalType.name)) {
                finalType = keywordMap.getValue(finalType.name)
            }
        }

        if (finalType is LiteralViejo) {
            val str = finalType.value
            // El type solo es asignado string type si arranca con comillas
            if (str.isNotEmpty() && charIsQuote(str.first())) {
                val last = str.last()
                if (!charIsQuote(last)) {
                    return Failure(LexerError.UNTERMINATED_STRING)
                } else {
                    finalType = LiteralViejo(str.substring(1, str.length - 1))
                }
            }
        }

        return Success(finalType)
    }
}
