package lexer.states

import domain.Either
import domain.Success
import lexer.LexerError

internal class DoubleQuoteStringState : State {
    override fun canConsume(chr: Char): Boolean =
        chr == '"'

    override fun consume(chr: Char): Either<LexerError, StateResult> {
        return when {
            chr == '"' -> Success(Done)
            else -> Success(Next(this))
        }
    }
}

internal class SingleQuoteStringState : State {
    override fun canConsume(chr: Char): Boolean =
        chr == '\''

    override fun consume(chr: Char): Either<LexerError, StateResult> {
        return when {
            chr == '\'' -> Success(Done)
            else -> Success(Next(this))
        }
    }
}
