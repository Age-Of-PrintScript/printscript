package states

import Done
import Either
import Failure
import LexerError
import State
import StateResult
import Success
import Next

// el lexer solo falla si aparece una palabra que no reconoce en su vocabulario

internal class IntegerState : State {
    override fun consume(chr: Char): Either<LexerError, StateResult> {
        return when {
            chr.isDigit() -> Success(Next(this, ))
            chr == '.' -> Success(Next(DecimalPointState()))
            else -> Success(Done)
        }
    }
}

internal class DecimalPointState: State {
    override fun consume(chr: Char): Either<LexerError, StateResult> {
        if (chr.isDigit()) return Success(Next(DecimalState()))
        return Failure(LexerError.LEXICAL_ERROR)
    }
}

internal class DecimalState: State {
    override fun consume(chr: Char): Either<LexerError, StateResult> {
        if (chr.isDigit()) return Success(Next(DecimalState()))
        return Success(Done)
    }
}
