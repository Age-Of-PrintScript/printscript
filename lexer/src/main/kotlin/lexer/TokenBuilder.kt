package lexer

import domain.Either
import domain.Failure
import domain.Position
import domain.PrintScriptValue.StringLiteral
import domain.Success
import domain.getOrReturn
import tokens.Identifier
import tokens.Literal
import tokens.Token
import tokens.TokenType
import tokens.WHITESPACE

internal class TokenBuilder {
    private var type: TokenType? = null
    private val tokenMap = createSymbolTokenMap()

    fun addChar(chr: Char): Either<LexerError, Unit> {
        // esto puede ser que se rompa xq antes checkeaba si el type era un String Literal y ahora
        // como maximo se puede checkear si es un Literal
        if (type is Literal && charIsNotQuote(chr)) {
            val newType = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
            type = newType
        }
        when {
            chr.isDigit() -> {
                if (type == null) type = Literal(chr.toString())
                else {
                    val newType = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    type = newType
                }
            }
            chr.isLetter() -> {
                if (type == null) type = Identifier(chr.toString())
                else {
                    val newType = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    type = newType
                }
            }
            chr == '.' -> {
                if (type == null || type is Identifier) return Failure(LexerError.INVALID_CHARACTER)
                else {
                    val newType = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    type = newType
                }
            }
            chr == '\'' || chr == '"'-> {
                if (type == null) type = Literal(chr.toString())
                // este tambien checkeaba si era un string literal
                else if (type is Literal) {
                    val newType = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    type = newType
                }
                else return Failure(LexerError.INVALID_CHARACTER)
            }
            chr.isWhitespace() -> type = WHITESPACE
            else -> {
                if (tokenMap.containsKey(chr)) type = tokenMap.getValue(chr)
                else return Failure(LexerError.INVALID_CHARACTER)
            }
        }
        return Success(Unit)
    }

    private fun updateTypeWithLiteral(type: TokenType?, chr: Char): Either<LexerError, TokenType> {
        return when (type) {
            is Identifier -> Success(Identifier(type.name + chr))
            is Literal ->  Success(Literal(type.value + chr))
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

        if (finishedType is Literal) {
            val str = finishedType.value
            val last = str.last()
            //El type solo es asignado string type si arranca con comillas
            if (charIsNotQuote(last)) return Failure(LexerError.UNTERMINATED_STRING)
            else finishedType = Literal(str.substring(1, str.length - 1))
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
