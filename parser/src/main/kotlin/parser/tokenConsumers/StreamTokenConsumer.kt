package parser.tokenConsumers

import domain.Position
import tokens.Token

class StreamTokenConsumer(
    private val tokenSupplier: () -> Token?,
) : TokenConsumer {
    private var buffer: Token? = null
    private var lastConsumed: Token? = null

    override fun hasNext(): Boolean {
        if (buffer != null) return true
        buffer = tokenSupplier()
        return buffer != null
    }

    override fun peek(): Token {
        if (buffer == null) {
            buffer = tokenSupplier()
        }
        return buffer!!
    }

    override fun consume(): Token {
        val token = peek()
        lastConsumed = token
        buffer = null
        return token
    }

    override val lastPosition: Position?
        get() = lastConsumed?.end ?: buffer?.end
}
