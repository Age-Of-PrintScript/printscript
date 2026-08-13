package lexer.states

import domain.Either
import domain.Success
import lexer.LexerError

internal class IdentifierState : State {
    override fun canConsume(chr: Char): Boolean = chr.isLetterOrDigit()

    override fun consume(chr: Char): Either<LexerError, StateResult> {
        if (chr.isLetterOrDigit()) return Success(Next(this))
        return Success(Done)
    }
}