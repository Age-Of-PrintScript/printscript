package parser.tokenConsumers

import tokens.Token

class ListTokenConsumer(
    val tokens: List<Token>,
) : TokenConsumer {
    private var position = 0

    override fun hasNext(): Boolean = position < tokens.size

    override fun peek(): Token = tokens[position]

    override fun consume(): Token = tokens[position++]
}
