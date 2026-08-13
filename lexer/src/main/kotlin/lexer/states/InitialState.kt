package lexer.states

import domain.Either
import domain.Failure
import domain.Success
import lexer.LexerError
import lexer.createSymbolStateMap

internal class InitialState: State {
    private val stateMap = createSymbolStateMap()
    override fun canConsume(chr: Char): Boolean {
        return chr.isDigit() ||
                chr.isLetter() ||
                chr == '\'' ||
                chr == '"' ||
                stateMap.containsKey(chr) ||
                chr.isWhitespace()
    }

    override fun consume(chr: Char): Either<LexerError, State> {
        return when {
            chr.isDigit() -> Success(IntegerState())
            chr.isLetter() -> Success(IdentifierState())
            chr == '\'' -> Success(SingleQuoteStringState())
            chr == '"' -> Success(EndStringLiteralState())
            chr.isWhitespace() -> Success(WhiteSpaceState())
            else -> {
                if (stateMap.containsKey(chr)) Success(stateMap.getValue(chr))
                else Failure(LexerError.INVALID_CHARACTER)
            }
        }
    }
}
