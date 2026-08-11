package lexer.states

import Either
import Success
import lexer.LexerError

internal class IdentifierState : State {
    override fun consume(chr: Char): Either<LexerError, StateResult> {
        if (chr.isLetterOrDigit()) return Success(Next(this))
        return Success(Done)
    }
}