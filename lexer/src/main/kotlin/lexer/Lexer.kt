package lexer

import domain.Either
import tokens.TokenList

interface Lexer {
    fun tokenize(source: String): Either<LexerError, TokenList>
}

class LexerImpl : Lexer {
    private val stateMachine = LexerStateMachine()

    override fun tokenize(source: String): Either<LexerError, TokenList> = stateMachine.tokenize(source)
}
