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
        if (type is Literal && !charIsQuote(chr)) {
            type = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
        }
        else {
            when {
                chr.isDigit() -> {
                    type =
                        if (type == null) Literal(chr.toString())
                        else
                            updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                }

                chr.isLetter() -> {
                    type =
                        if (type == null) Identifier(chr.toString())
                        else
                            updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                }

                chr == '.' -> {
                    if (type is Literal)
                        type = updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                    else return Failure(LexerError.INVALID_CHARACTER)
                }

                chr == '\'' || chr == '"' -> {
                    type =
                        if (type == null) Literal(chr.toString())
                        else if (type is Literal)
                            updateTypeWithLiteral(type, chr).getOrReturn { return Failure(it) }
                        else return Failure(LexerError.INVALID_CHARACTER)
                }

                chr.isWhitespace() -> type = WHITESPACE

                else -> {
                    if (tokenMap.containsKey(chr)) type = tokenMap.getValue(chr)
                    else return Failure(LexerError.INVALID_CHARACTER)
                }
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
            //El type solo es asignado string type si arranca con comillas
            if (str.isNotEmpty() && charIsQuote(str.first())) {
                val last = str.last()
                if (!charIsQuote(last)) return Failure(LexerError.UNTERMINATED_STRING)
                else finishedType = Literal(str.substring(1, str.length - 1))
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
