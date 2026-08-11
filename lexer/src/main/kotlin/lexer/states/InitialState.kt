package lexer.states

import Either
import Failure
import Success
import lexer.LexerError

internal class InitialState: State {
    override fun consume(chr: Char): Either<LexerError, StateResult> {
        return when {
            chr.isDigit() -> Success(Next(IntegerState()))
            chr.isLetter() -> Success(Next(IdentifierState()))
            chr == '\'' -> Success(Next(SingleQuoteStringState()))
            chr == '"' -> Success(Next(DoubleQuoteStringState()))
            chr == ':' -> Success(Done)
            chr == ';' -> Success(Done)
            chr == '=' -> Success(Done)
            chr == '+' -> Success(Done)
            chr == '-' -> Success(Done)
            chr == '*' -> Success(Done)
            chr == '/' -> Success(Done)
            chr == '(' -> Success(Done)
            chr == ')' -> Success(Done)
            else -> Failure(LexerError.INVALID_CHARACTER)
        }
    }
}
