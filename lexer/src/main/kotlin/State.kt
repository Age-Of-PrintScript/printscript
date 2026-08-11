internal interface State {
    fun consume(chr: Char): Either<LexerError, StateResult>
}

internal sealed interface StateResult

internal data class Next(val state: State): StateResult
internal object Done : StateResult
