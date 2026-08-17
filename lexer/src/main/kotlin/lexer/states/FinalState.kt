package lexer.states

import domain.Either
import domain.Failure
import lexer.LexerError

internal class FinalState : State {
    override fun canConsume(chr: Char): Boolean = false

    override fun consume(chr: Char): Either<LexerError, State> {
        return Failure(LexerError.INVALID_CHARACTER)
    }
}