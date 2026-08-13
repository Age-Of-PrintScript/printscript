package lexer.states

import domain.Either
import domain.Failure
import domain.Success
import lexer.LexerError
import lexer.createSymbolStateMap

internal class InitialState: State {
    private val stateMap = createSymbolStateMap()

    override fun consume(chr: Char): Either<LexerError, StateResult> {
        return when {
            chr.isDigit() -> Success(Next(IntegerState()))
            chr.isLetter() -> Success(Next(IdentifierState()))
            chr == '\'' -> Success(Next(SingleQuoteStringState()))
            chr == '"' -> Success(Next(DoubleQuoteStringState()))
            else -> {
                val symbol = chr.toString()
                if (stateMap.containsKey(symbol)) Success(stateMap.getValue(symbol))
                else Failure(LexerError.INVALID_CHARACTER)
            }
        }
    }
}
