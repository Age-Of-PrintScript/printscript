package lexer.states

import domain.Either
import domain.Failure
import domain.Success
import lexer.LexerError

internal class WhiteSpaceState : State {
    override fun canConsume(chr: Char): Boolean = chr.isWhitespace()

    override fun consume(chr: Char): Either<LexerError, State> {
        if (chr.isWhitespace()) return Success(this)
        return Failure(LexerError.INVALID_CHARACTER)
    }
}
