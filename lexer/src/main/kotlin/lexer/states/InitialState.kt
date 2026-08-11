package lexer.states

import Either
import Failure
import Success

internal class InitialState: State {
    override fun consume(chr: Char): Either<lexer.LexerError, StateResult> {
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
            else -> Failure(_root_ide_package_.lexer.LexerError.LEXICAL_ERROR)
        }
    }
}
