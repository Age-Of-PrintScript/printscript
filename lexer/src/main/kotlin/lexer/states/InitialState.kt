package lexer.states

import domain.Either
import domain.Failure
import domain.Success
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
