package lexer.states

import domain.Either
import lexer.LexerError

internal interface State {
    fun canConsume(chr: Char): Boolean
    fun consume(chr: Char): Either<LexerError, StateResult>
}

internal sealed interface StateResult

internal data class Next(val state: State): StateResult
internal object Done : StateResult
