package lexer

internal fun charIsNotQuote(chr: Char): Boolean =
    chr != '\'' && chr != '"'
