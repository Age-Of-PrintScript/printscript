package lexer

import tokens.TokenType

data class Lexicon(
    val symbols: Map<Char, TokenType>,
    val keywords: Map<String, TokenType>,
)
