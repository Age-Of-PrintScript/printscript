package states

import Done
import Either
import LexerError
import Next
import State
import StateResult
import Success

internal class IdentifierState : State {
    override fun consume(chr: Char): Either<LexerError, StateResult> {
        if (chr.isLetterOrDigit()) return Success(Next(this))
        return Success(Done)
    }
}
