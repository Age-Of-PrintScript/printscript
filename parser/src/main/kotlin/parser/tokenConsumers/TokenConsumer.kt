package parser.tokenConsumers

import domain.Either
import domain.Failure
import domain.Success
import parser.SyntaxError
import tokens.Token
import tokens.TokenType
import kotlin.reflect.KClass

interface TokenConsumer {
    fun hasNext(): Boolean

    fun peek(): Token

    fun consume(): Token

    companion object {
        operator fun invoke(tokens: List<Token>): TokenConsumer = ListTokenConsumer(tokens)

        fun from(tokens: List<Token>): TokenConsumer = ListTokenConsumer(tokens)

        fun from(supplier: () -> Token?): TokenConsumer = StreamTokenConsumer(supplier)
    }
}

fun TokenConsumer.consumeIf(expectedType: KClass<out TokenType>): Token? {
    if (!hasNext()) return null
    val token = peek()
    return if (expectedType.isInstance(token.type)) consume() else null
}

fun TokenConsumer.consumeExpected(
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

fun TokenConsumer.consumeUntil(delimiterType: KClass<out TokenType>): List<Token> {
    val accumulated = mutableListOf<Token>()
    while (hasNext() && !delimiterType.isInstance(peek().type)) {
        accumulated.add(consume())
    }
    return accumulated
}

fun TokenConsumer.consumeBalancedUntil(
    openDelimiter: KClass<out TokenType>,
    closeDelimiter: KClass<out TokenType>,
): List<Token> {
    val accumulated = mutableListOf<Token>()
    var depth = 1
    while (hasNext() && depth > 0) {
        val token = peek()
        when {
            openDelimiter.isInstance(token.type) -> {
                depth++
                accumulated.add(consume())
            }
            closeDelimiter.isInstance(token.type) -> {
                depth--
                if (depth > 0) {
                    accumulated.add(consume())
                }
            }
            else -> accumulated.add(consume())
        }
    }
    return accumulated
}
