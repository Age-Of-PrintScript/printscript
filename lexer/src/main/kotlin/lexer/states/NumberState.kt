package lexer.states

import domain.Either
import domain.Failure
import domain.Success
import lexer.LexerError

// el lexer solo falla si aparece una palabra que no reconoce en su vocabulario

internal class IntegerState : State {
    override fun canConsume(chr: Char): Boolean = chr.isDigit() || chr == '.'

    override fun consume(chr: Char): Either<LexerError, State> =
        when {
            chr.isDigit() -> Success(this)
            chr == '.' -> Success(DecimalPointState())
            else -> Failure(LexerError.INVALID_CHARACTER)
        }
}

internal class DecimalPointState : State {
    override fun canConsume(chr: Char): Boolean = chr.isDigit()

    override fun consume(chr: Char): Either<LexerError, State> {
        if (chr.isDigit()) return Success(DecimalState())
        return Failure(LexerError.INVALID_CHARACTER)
    }
}

internal class DecimalState : State {
    override fun canConsume(chr: Char): Boolean = chr.isDigit()

    override fun consume(chr: Char): Either<LexerError, State> {
        if (chr.isDigit()) return Success(DecimalState())
        return Failure(LexerError.INVALID_CHARACTER)
    }
}
