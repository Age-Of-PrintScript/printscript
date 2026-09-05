package lexer.states

import domain.Either
import domain.Failure
import domain.Success
import lexer.LexerError

internal class IdentifierState : State {
    override fun canConsume(chr: Char): Boolean = chr.isLetterOrDigit() || chr == '_'

    override fun consume(chr: Char): Either<LexerError, State> {
        if (chr.isLetterOrDigit() || chr == '_') return Success(this)
        return Failure(LexerError.INVALID_CHARACTER)
    }
}
