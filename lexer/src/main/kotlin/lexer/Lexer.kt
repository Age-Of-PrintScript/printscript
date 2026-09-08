package lexer

import domain.Either
import tokens.TokenList

interface Lexer {
    fun tokenize(source: String): Either<LexerError, TokenList>

    companion object {
        fun new(lexicon: Lexicon): Lexer = LexerImpl(lexicon)
    }
}

internal class LexerImpl(
    val lexicon: Lexicon,
) : Lexer {
    private val stateMachine = LexerStateMachine(lexicon)

    override fun tokenize(source: String): Either<LexerError, TokenList> = stateMachine.tokenize(source)
}
