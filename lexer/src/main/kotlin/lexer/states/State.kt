package lexer.states

import domain.Either
import lexer.LexerError

internal interface State {
    fun canConsume(chr: Char): Boolean

    fun consume(chr: Char): Either<LexerError, State>
}
