package lexer.states

import domain.Either
import domain.Failure
import domain.Success
import lexer.LexerError
import lexer.Lexicon

internal class InitialState(
    lexicon: Lexicon,
) : State {
    private val stateMap =
        lexicon.symbols.mapValues {
            FinalState()
        }

    override fun canConsume(chr: Char): Boolean =
        chr.isDigit() ||
            chr.isLetter() ||
            chr == '\'' ||
            chr == '"' ||
            stateMap.containsKey(chr) ||
            chr.isWhitespace()

    override fun consume(chr: Char): Either<LexerError, State> =
        when {
            chr.isDigit() -> Success(IntegerState())
            chr.isLetter() -> Success(IdentifierState())
            chr == '\'' || chr == '"' -> Success(StringState(chr))
            chr.isWhitespace() -> Success(WhiteSpaceState())
            else -> {
                if (stateMap.containsKey(chr)) {
                    Success(stateMap.getValue(chr))
                } else {
                    Failure(LexerError.INVALID_CHARACTER)
                }
            }
        }
}
