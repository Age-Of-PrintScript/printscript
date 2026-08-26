package lexer.states

import domain.Either
import domain.Success
import lexer.LexerError

internal class StringState(
    val quoteStyle: Char,
) : State {
    override fun canConsume(chr: Char): Boolean = true

    override fun consume(chr: Char): Either<LexerError, State> =
        if (chr == quoteStyle) {
            Success(FinalState())
        } else {
            Success(this)
        }
}
