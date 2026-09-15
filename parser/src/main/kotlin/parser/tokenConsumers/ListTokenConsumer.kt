package parser.tokenConsumers

import domain.Position
import tokens.Token

class ListTokenConsumer(
    val tokens: List<Token>,
) : TokenConsumer {
    private var position = 0

    override fun hasNext(): Boolean = position < tokens.size

    override fun peek(): Token = tokens[position]

    override fun consume(): Token = tokens[position++]

    override val lastPosition: Position?
        get() = if (position > 0) tokens[position - 1].end else tokens.lastOrNull()?.end
}
