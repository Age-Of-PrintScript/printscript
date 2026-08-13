package lexer.states

import domain.Either
import domain.Failure
import domain.Success
import lexer.LexerError

// el lexer solo falla si aparece una palabra que no reconoce en su vocabulario

internal class IntegerState : State {
    override fun canConsume(chr: Char): Boolean = chr.isDigit() || chr == '.'

    override fun consume(chr: Char): Either<LexerError, StateResult> {
        return when {
            chr.isDigit() -> Success(Next(this))
            chr == '.' -> Success(Next(DecimalPointState()))
            else -> Success(Done)
        }
    }
}

internal class DecimalPointState: State {
    override fun canConsume(chr: Char): Boolean = chr.isDigit()

    override fun consume(chr: Char): Either<LexerError, StateResult> {
        if (chr.isDigit()) return Success(Next(DecimalState()))
        return Failure(LexerError.UNRESOLVED_REFERENCE)
    }
}

internal class DecimalState: State {
    override fun canConsume(chr: Char): Boolean = chr.isDigit()

    override fun consume(chr: Char): Either<LexerError, StateResult> {
        if (chr.isDigit()) return Success(Next(DecimalState()))
        return Success(Done)
    }
}
