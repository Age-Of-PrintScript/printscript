interface Lexer {
    fun tokenize(source: String): List<Token>
}