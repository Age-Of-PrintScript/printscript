package lexer

internal fun charIsQuote(chr: Char): Boolean =
    chr == '\'' || chr == '"'