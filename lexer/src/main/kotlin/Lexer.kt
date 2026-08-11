import tokens.TokenList

interface Lexer {
    fun tokenize(source: String): Either<LexerError, TokenList>
}

class LexerImpl : Lexer {
    override fun tokenize(source: String): Either<LexerError, TokenList> {
        TODO("Not yet implemented")
    }
}

