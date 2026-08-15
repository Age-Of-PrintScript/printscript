package lexer.states

import domain.Either
import domain.Failure
import domain.Success
import lexer.LexerError


internal class StringState(val quoteStyle: Char) : State {
    override fun canConsume(chr: Char): Boolean = true
    override fun consume(chr: Char): Either<LexerError, State> {
        return if (chr == quoteStyle) Success(FinalState())
        else Success(this)
    }
}

