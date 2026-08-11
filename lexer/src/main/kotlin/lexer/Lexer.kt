package lexer

interface Lexer {
    fun tokenize(source: String): Either<LexerError, TokenList>
}

internal class LexerImpl : Lexer {
    private val automata = Automata()

    override fun tokenize(source: String): Either<LexerError, TokenList> {
        return automata.tokenize(source)
    }
}

