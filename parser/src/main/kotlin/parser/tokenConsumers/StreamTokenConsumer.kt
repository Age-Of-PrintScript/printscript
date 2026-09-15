package parser.tokenConsumers

import tokens.Token

class StreamTokenConsumer(
    private val tokenSupplier: () -> Token?,
) : TokenConsumer {
    private var buffer: Token? = null

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
        buffer = null
        return token
    }
}
