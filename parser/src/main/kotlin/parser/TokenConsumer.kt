package parser

import tokens.Token

class TokenConsumer(val tokens: List<Token>) {
    private var position = 0
    fun hasNext() = position < tokens.size
    fun peek() = tokens[position]
    fun consume() = tokens[position++]
}
