package states

import Done
import Either
import LexerError
import State
import StateResult
import Success
import Next

internal class DoubleQuoteStringState : State {
    override fun consume(chr: Char): Either<LexerError, StateResult> {
        return when {
            chr == '\'' -> Success(Done)
            else -> Success(Next(this))
        }
    }
}

internal class SingleQuoteStringState : State {
    override fun consume(chr: Char): Either<LexerError, StateResult> {
        return when {
            chr == '"' -> Success(Done)
            else -> Success(Next(this))
        }
    }
}
