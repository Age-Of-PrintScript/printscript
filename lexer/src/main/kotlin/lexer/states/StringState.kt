package lexer.states

import Either
import Success

internal class DoubleQuoteStringState : State {
    override fun consume(chr: Char): Either<lexer.LexerError, StateResult> {
        return when {
            chr == '\'' -> Success(Done)
            else -> Success(Next(this))
        }
    }
}

internal class SingleQuoteStringState : State {
    override fun consume(chr: Char): Either<lexer.LexerError, StateResult> {
        return when {
            chr == '"' -> Success(Done)
            else -> Success(Next(this))
        }
    }
}
