package parser

import domain.Either
import domain.Failure
import domain.Success
import tokens.Token
import tokens.TokenType
import kotlin.reflect.KClass

class TokenConsumer(
    val tokens: List<Token>,
) {
    private var position = 0

    fun hasNext(): Boolean = position < tokens.size

    fun peek(): Token = tokens[position]

    fun peekOrNull(): Token? = tokens.getOrNull(position)

    fun consume(): Token = tokens[position++]

    fun consumeIf(expectedType: KClass<out TokenType>): Token? {
        if (!hasNext()) return null
        val token = peek()
        return if (expectedType.isInstance(token.type)) consume() else null
    }

    fun consumeExpected(
        expectedType: KClass<out TokenType>,
        errorOnMismatch: SyntaxError,
    ): Either<SyntaxError, Token> {
        if (!hasNext()) return Failure(SyntaxError.INCOMPLETE_STATEMENT)
        val token = peek()
        if (!expectedType.isInstance(token.type)) {
            return Failure(errorOnMismatch)
        }
        return Success(consume())
    }

    fun consumeUntil(delimiterType: KClass<out TokenType>): List<Token> {
        val accumulated = mutableListOf<Token>()
        while (hasNext() && !delimiterType.isInstance(peek().type)) {
            accumulated.add(consume())
        }
        return accumulated
    }
}
