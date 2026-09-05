package lexer

import domain.Either
import tokens.TokenList

interface Lexer {
    fun tokenize(source: String): Either<LexerError, TokenList>

    companion object {
        fun new(): Lexer = LexerImpl()
    }
}

internal class LexerImpl : Lexer {
    private val stateMachine = LexerStateMachine()

    override fun tokenize(source: String): Either<LexerError, TokenList> = stateMachine.tokenize(source)
}
