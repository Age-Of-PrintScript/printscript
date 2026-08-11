package lexer.states

import Either
import Failure
import Success

// el lexer solo falla si aparece una palabra que no reconoce en su vocabulario

internal class IntegerState : State {
    override fun consume(chr: Char): Either<lexer.LexerError, StateResult> {
        return when {
            chr.isDigit() -> Success(Next(this))
            chr == '.' -> Success(Next(DecimalPointState()))
            else -> Success(Done)
        }
    }
}

internal class DecimalPointState: State {
    override fun consume(chr: Char): Either<lexer.LexerError, StateResult> {
        if (chr.isDigit()) return Success(Next(DecimalState()))
        return Failure(_root_ide_package_.lexer.LexerError.LEXICAL_ERROR)
    }
}

internal class DecimalState: State {
    override fun consume(chr: Char): Either<lexer.LexerError, StateResult> {
        if (chr.isDigit()) return Success(Next(DecimalState()))
        return Success(Done)
    }
}
