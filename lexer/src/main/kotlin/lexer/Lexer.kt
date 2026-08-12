package lexer

import domain.Either
import tokens.TokenList

interface Lexer {
    fun tokenize(source: String): Either<LexerError, TokenList>
}

internal class LexerImpl : Lexer {
    private val stateMachine = LexerStateMachine()

    override fun tokenize(source: String): Either<LexerError, TokenList> {
        return stateMachine.tokenize(source)
    }
}

